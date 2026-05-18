package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.BannerDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Banner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(name = "AdminBannerServlet", urlPatterns = {"/admin/banner-manager"})
@MultipartConfig
public class AdminBannerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        BannerDAO dao = new BannerDAO();

        if ("add".equals(action)) {
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            String linkUrl = request.getParameter("linkUrl");
            String startStr = request.getParameter("startDate");
            String endStr = request.getParameter("endDate");


//            String imageUrl = saveFile(filePart);

            Banner b = new Banner();
//            b.setImageUrl(imageUrl);
            b.setLinkUrl(linkUrl);
            b.setSortOrder(99);
            b.setActive(true);
            dao.insertBanner(b);
            response.sendRedirect("banner-manager");

        } else if ("updateOrder".equals(action)) {
            String[] ids = request.getParameterValues("ids[]");
            for (int i = 0; i < ids.length; i++) {
                dao.updateBannerOrder(Integer.parseInt(ids[i]), i + 1);
            }
            response.getWriter().write("success");
        }
    }
    private String saveFile(Part filePart, HttpServletRequest request) throws IOException {
        String folderPath = "assets/images/banners";

        String uploadPath = request.getServletContext().getRealPath("/") + File.separator + folderPath;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadPath + File.separator + uniqueFileName;
        filePart.write(filePath);
        return folderPath + "/" + uniqueFileName;
    }

}