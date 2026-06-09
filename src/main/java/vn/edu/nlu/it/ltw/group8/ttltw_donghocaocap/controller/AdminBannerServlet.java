package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.BannerDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Banner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "AdminBannerServlet", urlPatterns = {"/admin/banner-manager"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AdminBannerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BannerDAO dao = new BannerDAO();
        List<Banner> listSlideshow = dao.getAllBanners();
        request.setAttribute("listSlideshow", listSlideshow);

        request.getRequestDispatcher("/admin/manage-banner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        BannerDAO dao = new BannerDAO();

        // 1. XỬ LÝ THEO MỚI BANNER (CÓ FILE ẢNH, LINK, HẸN GIỜ)
        if ("add".equals(action)) {
            try {
                Part filePart = request.getPart("image");
                String linkUrl = request.getParameter("linkUrl");
                String startStr = request.getParameter("startDate");
                String endStr = request.getParameter("endDate");
                String sortOrderStr = request.getParameter("sortOrder");

                String imageUrl = saveFile(filePart, request);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date startDate = (startStr != null && !startStr.trim().isEmpty()) ? sdf.parse(startStr) : null;
                Date endDate = (endStr != null && !endStr.trim().isEmpty()) ? sdf.parse(endStr) : null;
                int sortOrder = (sortOrderStr != null && !sortOrderStr.trim().isEmpty()) ? Integer.parseInt(sortOrderStr) : 99;

                Banner b = new Banner();
                b.setImageUrl(imageUrl);
                b.setLinkUrl(linkUrl);
                b.setSortOrder(sortOrder);
                b.setActive(true);
                b.setStartDate(startDate);
                b.setEndDate(endDate);

                dao.insertBanner(b);
                response.sendRedirect("banner-manager?status=add_success");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("banner-manager?status=error");
            }

        } else if ("updateOrder".equals(action)) {
            response.setContentType("text/plain");
            String[] ids = request.getParameterValues("ids[]");
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    dao.updateBannerOrder(Integer.parseInt(ids[i]), i + 1);
                }
                response.getWriter().write("success");
            } else {
                response.getWriter().write("fail");
            }

        } else if ("deleteBanner".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteBanner(id);
            response.sendRedirect("banner-manager?status=delete_success");
        }
    }

    private String saveFile(Part filePart, HttpServletRequest request) throws IOException {
        String folderPath = "assets/img/banners";
        String uploadPath = request.getServletContext().getRealPath("/") + File.separator + folderPath;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadPath + File.separator + uniqueFileName;
        filePart.write(filePath);
        return folderPath + "/" + uniqueFileName;
    }

}