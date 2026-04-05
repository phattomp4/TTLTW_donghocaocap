package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category"})
public class CategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();

        String action = request.getParameter("action");
        String legacyType = request.getParameter("type");
        String legacyName = request.getParameter("name");

        List<String> typesList = new ArrayList<>();
        List<String> brandsList = new ArrayList<>();
        List<String> gendersList = new ArrayList<>();
        String priceFilter = "";
        String collectionFilter = "";

        if ("filter".equals(action)) {
            if (request.getParameterValues("typeFilter") != null) typesList.addAll(Arrays.asList(request.getParameterValues("typeFilter")));
            if (request.getParameterValues("brandFilter") != null) brandsList.addAll(Arrays.asList(request.getParameterValues("brandFilter")));
            if (request.getParameterValues("genderFilter") != null) gendersList.addAll(Arrays.asList(request.getParameterValues("genderFilter")));
            priceFilter = request.getParameter("priceFilter");
            collectionFilter = request.getParameter("collectionFilter");
            if ("true".equals(request.getParameter("hidePriceBlock"))) {
                request.setAttribute("hidePriceBlock", true);
            }
        }

        else {
            if ("search".equals(legacyType)) {
                String keyword = request.getParameter("keyword");
                List<Product> searchList = dao.searchProducts(keyword);

                request.setAttribute("listProduct", searchList);
                request.setAttribute("pageTitle", "Kết quả tìm kiếm: " + (keyword != null ? keyword : ""));

                String currentType = "";
                boolean isAccessory = false;
                if (keyword != null) {
                    String kw = keyword.toLowerCase();
                    if (kw.contains("day") || kw.contains("dây")) {
                        currentType = "Dây"; isAccessory = true;
                    } else if (kw.contains("hop") || kw.contains("hộp")) {
                        currentType = "Hộp"; isAccessory = true;
                    } else if (kw.contains("phụ kiện") || kw.contains("phu kien")) {
                        currentType = "Phụ kiện"; isAccessory = true;
                    }
                }

                if (!currentType.isEmpty()) request.setAttribute("selectedTypes", currentType);
                request.setAttribute("isAccessoryOnly", isAccessory);

                request.setAttribute("mapBrands", dao.getAllBrandsWithLogo());
                request.getRequestDispatcher("product-list.jsp").forward(request, response);
                return;
            }

            else if ("price".equals(legacyType)) {
                try {
                    double min = Double.parseDouble(request.getParameter("min"));
                    double max = Double.parseDouble(request.getParameter("max"));

                    if (min == 0 && max <= 1000000) priceFilter = "under1";
                    else if (min == 1000000 && max <= 3000000) priceFilter = "1to3";
                    else if (min == 3000000 && max <= 6000000) priceFilter = "3to6";
                    else if (min == 6000000 && max <= 9000000) priceFilter = "6to9";
                    else if (min == 9000000 && max <= 15000000) priceFilter = "9to15";
                    else if (min >= 15000000) priceFilter = "over15";

                    typesList.add("Đồng hồ");

                    request.setAttribute("hidePriceBlock", true);

                    String customTitle = "";
                    if (min == 0) customTitle = "Đồng hồ dưới " + (long)(max/1000000) + " triệu";
                    else if (max == -1) customTitle = "Đồng hồ trên " + (long)(min/1000000) + " triệu";
                    else customTitle = "Đồng hồ từ " + (long)(min/1000000) + " - " + (long)(max/1000000) + " triệu";

                    request.setAttribute("overrideTitle", customTitle);
                } catch (Exception e) { e.printStackTrace(); }
            }

            else if ("collection".equals(legacyType) && legacyName != null) {
                if (legacyName.toLowerCase().contains("luxury") || legacyName.toLowerCase().contains("nổi bật") || legacyName.toLowerCase().contains("bán chạy")) {
                    if (legacyName.toLowerCase().contains("luxury")) collectionFilter = "luxury";
                    else if (legacyName.toLowerCase().contains("nổi bật") || legacyName.toLowerCase().contains("noi bat")) collectionFilter = "noibat";
                    else if (legacyName.toLowerCase().contains("bán chạy") || legacyName.toLowerCase().contains("ban chay")) collectionFilter = "banchay";

                    typesList.add("Đồng hồ");
                    request.setAttribute("overrideTitle", "Bộ sưu tập: " + legacyName);
                }
                else {
                    List<Product> collectionList = new ArrayList<>();
                    if (legacyName.toLowerCase().contains("nhật")) collectionList = dao.getProductsByOrigin("Nhật");
                    else if (legacyName.toLowerCase().contains("thụy")) collectionList = dao.getProductsByOrigin("Thụy");
                    else collectionList = dao.searchProducts(legacyName);

                    request.setAttribute("listProduct", collectionList);
                    request.setAttribute("pageTitle", "Bộ sưu tập: " + legacyName);
                    request.setAttribute("isAccessoryOnly", false);
                    request.setAttribute("mapBrands", dao.getAllBrandsWithLogo());
                    request.getRequestDispatcher("product-list.jsp").forward(request, response);
                    return;
                }
            }

            else {
                if ("brand".equals(legacyType) && legacyName != null) {
                    brandsList.add(legacyName);
                    typesList.add("Đồng hồ");
                }
                else if ("nam".equals(legacyType)) {
                    gendersList.add("Nam");
                    typesList.add("Đồng hồ");
                } else if ("nu".equals(legacyType)) {
                    gendersList.add("Nữ");
                    typesList.add("Đồng hồ");
                } else if ("accessories".equals(legacyType)) {
                    typesList.add("Phụ kiện");
                    request.setAttribute("isAccessoryOnly", true);
                }
                else if ("dongho".equals(legacyType)) {
                    typesList.add("Đồng hồ");
                }
                else {
                    typesList.add("Đồng hồ");
                }
            }
        }


        boolean isAccessoryOnly = typesList.contains("Dây") || typesList.contains("Hộp") || typesList.contains("Phụ kiện");
        if (typesList.contains("Đồng hồ")) isAccessoryOnly = false; // nếu có đồng hồ thì vẫn hiện full bộ lọc

        int activeCount = typesList.size() + brandsList.size() + gendersList.size() + (priceFilter != null && !priceFilter.isEmpty() ? 1 : 0);
        String title = "";

        if (activeCount > 2 || (activeCount == 2 && !typesList.contains("Đồng hồ"))) {
            title = ""; // có nhiều lựa chọn thì bỏ trống tiêu đề
        } else {
            if (brandsList.size() == 1) title = "Đồng hồ " + brandsList.get(0);
            else if (gendersList.size() == 1) title = "Đồng hồ " + gendersList.get(0);
            else if (typesList.size() == 1) title = "Danh mục " + typesList.get(0);
            else title = "Tất cả sản phẩm";
        }

        Map<String, String> mapBrands = dao.getAllBrandsWithLogo();
        List<Product> list = dao.filterProducts(
                typesList.isEmpty() ? null : typesList.toArray(new String[0]),
                priceFilter,
                brandsList.isEmpty() ? null : brandsList.toArray(new String[0]),
                gendersList.isEmpty() ? null : gendersList.toArray(new String[0]),
                collectionFilter
        );

        request.setAttribute("selectedTypes", String.join(",", typesList));
        request.setAttribute("selectedBrands", String.join(",", brandsList));
        request.setAttribute("selectedGenders", String.join(",", gendersList));
        request.setAttribute("selectedPrice", priceFilter);
        request.setAttribute("selectedCollection", collectionFilter);
        request.setAttribute("isAccessoryOnly", isAccessoryOnly);
        request.setAttribute("mapBrands", mapBrands);
        request.setAttribute("listProduct", list);
        request.setAttribute("pageTitle", title);

        request.getRequestDispatcher("product-list.jsp").forward(request, response);
    }
}