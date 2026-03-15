package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.ProductSpecification;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductFormServlet", urlPatterns = {"/admin/product-form"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class ProductFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");

        if (idRaw != null) {
            try {
                int pid = Integer.parseInt(idRaw);
                AdminDAO dao = new AdminDAO();


                Product p = dao.getProductById(pid);
                request.setAttribute("product", p);

                List<String> detailImages = dao.getDetailImages(pid);
                request.setAttribute("detailImages", detailImages);


                Map<String, String> specMap = dao.getProductSpecsMap(pid);
                request.setAttribute("specMap", specMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");


            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            double price = Double.parseDouble(request.getParameter("price"));
            double originalPrice = Double.parseDouble(request.getParameter("originalPrice"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String desc = request.getParameter("description");
            boolean isLuxury = request.getParameter("isLuxury") != null;


            Product p = new Product();
            p.setName(name);
            p.setSku(sku);
            p.setCurrentPrice(price);
            p.setOriginalPrice(originalPrice);
            p.setStockQuantity(stock);
            p.setDescription(desc);
            p.setLuxury(isLuxury);


            String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "products";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dnrpxyuwo",
                    "api_key", "261138144329333",
                    "api_secret", "beBh1tv2UJYTuS8CWkVmKS48CO4"
            ));


            Part mainPart = request.getPart("mainImage");
            if (mainPart != null && mainPart.getSize() > 0) {
                try {
                    byte[] fileBytes = mainPart.getInputStream().readAllBytes();

                    Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                            "folder", "vvp_store_products"
                    ));

                    String secureUrl = (String) uploadResult.get("secure_url");

                    p.setImageUrl(secureUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<String> detailImages = new ArrayList<>();
            Collection<Part> parts = request.getParts();

            for (Part part : parts) {
                if (part.getName().equals("detailImages") && part.getSize() > 0) {
                    try {
                        byte[] fileBytes = part.getInputStream().readAllBytes();

                        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                                "folder", "vvp_store_products"
                        ));

                        detailImages.add((String) uploadResult.get("secure_url"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            List<ProductSpecification> listSpecs = new ArrayList<>();
            String[] specNames = request.getParameterValues("specName");
            String[] specValues = request.getParameterValues("specValue");

            if (specNames != null && specValues != null) {
                for (int i = 0; i < specNames.length; i++) {
                    if (!specValues[i].trim().isEmpty()) {
                        ProductSpecification spec = new ProductSpecification();
                        spec.setName(specNames[i]);
                        spec.setValue(specValues[i]);
                        listSpecs.add(spec);
                    }
                }
            }

            AdminDAO dao = new AdminDAO();

            String idRaw = request.getParameter("id");

            if (idRaw != null && !idRaw.trim().isEmpty() && !idRaw.equals("0")) {
                int pid = Integer.parseInt(idRaw);
                p.setId(pid);

                dao.updateFullProduct(p, detailImages, listSpecs);
            } else {
                dao.insertFullProduct(p, detailImages, listSpecs);
            }

            response.sendRedirect("product-manager");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("product-form?error=1");
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}