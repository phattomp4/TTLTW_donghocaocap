package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/admin/export-statistics")
public class ExportStatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Nhận tham số period (giống như trang dashboard để xuất đúng giai đoạn)
        String period = request.getParameter("period");
        if (period == null || period.isEmpty()) {
            period = "month"; // Mặc định nếu không truyền là tháng này
        }

        // 2. Tạo một Workbook Excel mới (.xlsx)
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo tổng quan");

            // 3. Tạo định dạng chữ in đậm cho Header
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex()); // Màu xanh giống admin của bạn
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 4. Tạo dòng Tiêu đề (Header Row)
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Chỉ số hệ thống", "Giá trị thống kê", "Chu kỳ áp dụng"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // 5. Lấy dữ liệu thực tế (Bạn nên gọi từ DAO/Service của bạn lên thay vì gán cứng)
            // Ví dụ này mô phỏng lấy dữ liệu tương tự các biến ${revenue}, ${totalOrders} trên Dashboard
            long revenueData = 150000000; // Giả sử gọi từ hàm: watchService.getRevenue(period);
            int ordersData = 45;          // Giả sử gọi từ hàm: watchService.getTotalOrders(period);
            int lowStockData = 12;        // Giả sử gọi từ hàm: watchService.getLowStockCount();

            // Chuyển đổi tên chu kỳ hiển thị cho đẹp trong Excel
            String periodLabel = period.equals("today") ? "Hôm nay" :
                    period.equals("week") ? "Tuần này" :
                            period.equals("year") ? "Năm nay" : "Tháng này";

            // Dòng 1: Doanh thu
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("Tổng Doanh Thu");
            Cell revenueCell = row1.createCell(1);
            revenueCell.setCellValue(revenueData);
            // Định dạng tiền tệ cho ô doanh thu trong Excel
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0\" ₫\""));
            revenueCell.setCellStyle(currencyStyle);
            row1.createCell(2).setCellValue(periodLabel);

            // Dòng 2: Đơn hàng hoàn thành
            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("Tổng Đơn Hoàn Thành (Đơn giai đoạn)");
            row2.createCell(1).setCellValue(ordersData);
            row2.createCell(2).setCellValue(periodLabel);

            // Dòng 3: Sản phẩm sắp hết hàng
            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("Sản Phẩm Sắp Hết Hàng (< 3 chiếc)");
            row3.createCell(1).setCellValue(lowStockData);
            row3.createCell(2).setCellValue("Tất cả thời gian");

            // Tự động căn chỉnh độ rộng các cột cho vừa văn bản
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 6. Cấu hình HTTP Response để trình duyệt hiểu đây là file tải về
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=Bao_Cao_He_Thong_" + period + ".xlsx");

            // 7. Ghi dữ liệu ra luồng xuất (OutputStream) của phản hồi
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