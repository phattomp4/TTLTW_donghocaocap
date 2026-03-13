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
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.HomeDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.InterfaceDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ShopDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.ShopInfo;


import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AdminInterfaceServlet", urlPatterns = {"/admin/interface-manager"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AdminInterfaceServlet extends HttpServlet {


    private Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnrpxyuwo", // Thay bằng tên cloud của bạn
                "api_key", "261138144329333", // Thay bằng API Key của bạn
                "api_secret", "beBh1tv2UJYTuS8CWkVmKS48CO4" // Thay bằng API Secret của bạn
        ));
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShopDAO shopDao = new ShopDAO();
        HomeDAO homeDao = new HomeDAO();


        request.setAttribute("shopInfo", shopDao.getShopInfo());
        request.setAttribute("listSlideshow", homeDao.getSlideshowBanners());
        request.setAttribute("listSmallBanners", homeDao.getSmallBanners());
        request.setAttribute("listBrands", homeDao.getFeaturedBrands());

        request.getRequestDispatcher("/admin/interface-manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        InterfaceDAO dao = new InterfaceDAO();
        Cloudinary cloudinary = getCloudinary();

        try {

            if ("updateInfo".equals(action)) {
                ShopInfo info = new ShopInfo();
                info.setBrandName(request.getParameter("brandName"));
                info.setSubtitle(request.getParameter("subtitle"));
                info.setHistory1(request.getParameter("history1"));
                info.setHistory2(request.getParameter("history2"));
                info.setHistory3(request.getParameter("history3"));
                info.setFooterDesc(request.getParameter("footerDesc"));
                info.setAddress(request.getParameter("address"));
                info.setHotline(request.getParameter("hotline"));
                info.setEmail(request.getParameter("email"));
                info.setCopyright(request.getParameter("copyright"));

                Part filePart = request.getPart("mainImage");
                String oldImg = request.getParameter("oldMainImage");
                if (filePart != null && filePart.getSize() > 0) {
                    Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream().readAllBytes(), ObjectUtils.asMap("folder", "vvp_interface"));
                    info.setMainImageUrl((String) uploadResult.get("secure_url"));
                } else {
                    info.setMainImageUrl(oldImg);
                }

                dao.updateShopInfo(info);
                request.getSession().setAttribute("shopInfo", info); // Cập nhật lại session
            }


            else if ("addBanner".equals(action)) {
                Part filePart = request.getPart("bannerImage");
                int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
                if (filePart != null && filePart.getSize() > 0) {
                    Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream().readAllBytes(), ObjectUtils.asMap("folder", "vvp_banners"));
                    dao.addBanner((String) uploadResult.get("secure_url"), sortOrder);
                }
            }
            else if ("deleteBanner".equals(action)) {
                dao.deleteBanner(Integer.parseInt(request.getParameter("id")));
            }


            else if ("addSmallBanner".equals(action)) {
                Part filePart = request.getPart("smallImage");
                int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
                if (filePart != null && filePart.getSize() > 0) {
                    Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream().readAllBytes(), ObjectUtils.asMap("folder", "vvp_banners"));
                    dao.addSmallBanner((String) uploadResult.get("secure_url"), sortOrder);
                }
            }
            else if ("deleteSmallBanner".equals(action)) {
                dao.deleteSmallBanner(Integer.parseInt(request.getParameter("id")));
            }


            else if ("addBrand".equals(action)) {
                String name = request.getParameter("brandName");
                int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
                Part filePart = request.getPart("brandLogo");
                if (filePart != null && filePart.getSize() > 0) {
                    Map uploadResult = cloudinary.uploader().upload(filePart.getInputStream().readAllBytes(), ObjectUtils.asMap("folder", "vvp_brands"));
                    dao.addBrand(name, (String) uploadResult.get("secure_url"), sortOrder);
                }
            }
            else if ("deleteBrand".equals(action)) {
                dao.deleteBrand(Integer.parseInt(request.getParameter("id")));
            }

            response.sendRedirect("interface-manager?msg=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("interface-manager?msg=error");
        }
    }
}