package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.filter;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CategoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "MenuFilter", urlPatterns = {"/*"})
public class MenuFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        if (request.getServletContext().getAttribute("menuBrands") == null) {
            CategoryDAO dao = new CategoryDAO();

            request.getServletContext().setAttribute("menuBrands", dao.getCategoriesByParent(1));

            request.getServletContext().setAttribute("menuCollections", dao.getCategoriesByParent(2));

            request.getServletContext().setAttribute("menuAccessories", dao.getCategoriesByParent(25));

            request.getServletContext().setAttribute("menuPrices", dao.getAllPriceRanges());
        }
        chain.doFilter(request, response);
    }
}