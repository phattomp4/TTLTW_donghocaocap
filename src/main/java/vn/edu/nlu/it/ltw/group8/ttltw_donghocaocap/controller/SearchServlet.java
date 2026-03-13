package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
    import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

    import java.io.IOException;
    import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String keyword = req.getParameter("keyword");
        if (keyword == null) keyword = "";

        List<Product> list = productDAO.searchProducts(keyword);

        String ajax = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(ajax)) {
            resp.setContentType("text/html; charset=UTF-8");
            StringBuilder sb = new StringBuilder();

            if (list.isEmpty()) {
                sb.append("<div class='no-result'>Không tìm thấy sản phẩm</div>");
            } else {
                for (Product p : list) {
                    sb.append("<a href='productdetail?id=").append(p.getId()).append("'>");
                    sb.append("<div class='search-item'>");
                    sb.append("<img src='").append(p.getImageUrl()).append("' width='50'>");
                    sb.append("<span>").append(p.getName()).append("</span>");
                    sb.append("</div>");
                    sb.append("</a>");
                }
            }
            resp.getWriter().write(sb.toString());
            return;
        }

        req.setAttribute("keyword", keyword);
        req.setAttribute("list", list);
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    }
}