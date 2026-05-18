package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String LOG_DIR = "C:/VVP_Store/logs";
    private static final String LOG_FILE = LOG_DIR + "/payment_errors.log";

    public static void log(int orderId, double amount, String transactionId, String errorMessage) {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] CRITICAL ERROR: Đã thanh toán nhưng lỗi DB | OrderID: %d | Amount: %.2f | TransID: %s | Error: %s\n",
                timestamp, orderId, amount, transactionId, errorMessage);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            System.out.println(">>> [FileLogger] Đã ghi log lỗi thanh toán vào: " + LOG_FILE);
        } catch (IOException e) {
            System.err.println(">>> [FileLogger] KHÔNG THỂ GHI LOG: " + e.getMessage());
            e.printStackTrace();
        }
    }
}