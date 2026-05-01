package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Voucher;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.VoucherUsageDTO;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "VoucherManagerServlet", urlPatterns = {"/admin/voucher-manager"})
public class VoucherManagerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        List<VoucherUsageDTO> history = dao.getUsageHistory();
        request.setAttribute("voucherHistory", history);

        request.getRequestDispatcher("/admin/voucher-manager.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String type = request.getParameter("discountType");
        double value = Double.parseDouble(request.getParameter("discountValue"));
        double max = Double.parseDouble(request.getParameter("maxDiscount"));
        int limit = Integer.parseInt(request.getParameter("usageLimit"));
        Timestamp start = Timestamp.valueOf(request.getParameter("startDate").replace("T", " ") + ":00");
        Timestamp end = Timestamp.valueOf(request.getParameter("endDate").replace("T", " ") + ":00");

        Voucher v = new Voucher(0, code, type, value, max, limit, 0, start, end);
        AdminDAO dao = new AdminDAO();
        if(dao.addVoucher(v)) {
            response.sendRedirect("voucher-manager?status=success");
        } else {
            response.sendRedirect("voucher-manager?status=error");
        }
    }

}