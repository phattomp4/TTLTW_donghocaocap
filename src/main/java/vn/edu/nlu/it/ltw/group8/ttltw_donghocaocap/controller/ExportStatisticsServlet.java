package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.StatisticDAO;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/admin/export-statistics")
public class ExportStatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String period = request.getParameter("period");
        if (period == null || period.isEmpty()) {
            period = "month";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo tổng quan");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Chỉ số hệ thống", "Giá trị thống kê", "Chu kỳ áp dụng"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            StatisticDAO statDao = new StatisticDAO();
            double revenueData = statDao.getRevenueByPeriod(period);
            int ordersData = statDao.countOrdersByPeriod(period);
            int lowStockData = statDao.countLowStockProducts(10);


            String periodLabel = period.equals("today") ? "Hôm nay" :
                    period.equals("week") ? "Tuần này" :
                            period.equals("year") ? "Năm nay" : "Tháng này";

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("Tổng Doanh Thu");
            Cell revenueCell = row1.createCell(1);
            revenueCell.setCellValue(revenueData);
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0\" ₫\""));
            revenueCell.setCellStyle(currencyStyle);
            row1.createCell(2).setCellValue(periodLabel);

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("Tổng Đơn Hoàn Thành (Đơn giai đoạn)");
            row2.createCell(1).setCellValue(ordersData);
            row2.createCell(2).setCellValue(periodLabel);

            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("Sản Phẩm Sắp Hết Hàng (< 10 chiếc)");
            row3.createCell(1).setCellValue(lowStockData);
            row3.createCell(2).setCellValue("Tất cả thời gian");

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=Bao_Cao_He_Thong_" + period + ".xlsx");

            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Có lỗi xảy ra khi xuất báo cáo: " + e.getMessage());
        }
    }
}