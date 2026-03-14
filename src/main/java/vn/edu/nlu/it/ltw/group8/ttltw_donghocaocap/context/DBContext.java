package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    public Connection getConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/DongHoCaoCap?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String pass = "Truongvu3003@5";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    public static void main(String[] args) {
        try {
            System.out.println(new DBContext().getConnection());
            System.out.println("Kết nối thành công!");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}