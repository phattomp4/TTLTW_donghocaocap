package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.HomeDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ShopDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDao = new ProductDAO();
        HomeDAO homeDao = new HomeDAO();
        ShopDAO shopDao = new ShopDAO();

        HttpSession session = request.getSession();


        if (session.getAttribute("shopInfo") == null) {
            ShopInfo info = shopDao.getShopInfo();
            session.setAttribute("shopInfo", info);

            List<String> gallery = shopDao.getShopGallery();
            session.setAttribute("shopGallery", gallery);
        }

        List<Product> listFeatured = productDao.getFeaturedProducts();
        List<Product> listMen = productDao.getMenProducts();
        List<Product> listWomen = productDao.getWomenProducts();
        List<Product> listLuxury = productDao.getLuxuryProducts();

        List<SmallBanner> listSmallBanners = homeDao.getSmallBanners();
        List<Banner> listSlideshow = homeDao.getSlideshowBanners();
        List<Brand> listBrands = homeDao.getFeaturedBrands();


        request.setAttribute("listFeatured", listFeatured);
        request.setAttribute("listMen", listMen);
        request.setAttribute("listWomen", listWomen);
        request.setAttribute("listLuxury", listLuxury);

        request.setAttribute("listSmallBanners", listSmallBanners);
        request.setAttribute("listSlideshow", listSlideshow);
        request.setAttribute("listBrands", listBrands);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}