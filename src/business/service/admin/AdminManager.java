package business.service.admin;

import entity.Admin;
import validate.ValidateAdmin;

import java.io.*;
import java.util.Scanner;

public class AdminManager {
    private static final String SESSION_FILE = "admin_session.txt";
    private Admin currentAdmin;

    public AdminManager() {
        loadSession(); // Tự động tải phiên đăng nhập khi khởi tạo
    }

    // Kiểm tra xem đã có phiên đăng nhập không
    public boolean hasActiveSession() {
        return currentAdmin != null;
    }

    // Lấy thông tin admin hiện tại
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    // Đăng nhập admin
    public boolean loginAdmin() {
        // Nếu đã có phiên đăng nhập, trả về true luôn
        if (hasActiveSession()) {
            System.out.println("Bạn đã đăng nhập với tài khoản: " + currentAdmin.getUsername());
            return true;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== ĐĂNG NHẬP ADMIN ===");

        boolean isValid = ValidateAdmin.validateLogin(sc);

        if (isValid) {
            // Tạo đối tượng admin mặc định
            currentAdmin = Admin.getDefaultAdmin();
            saveSession(); // Lưu phiên đăng nhập
            System.out.println("Đăng nhập thành công!");
            return true;
        } else {
            System.err.println("Sai username hoặc password!");
            return false;
        }
    }

    // Đăng xuất admin
    public void logoutAdmin() {
        currentAdmin = null;
        clearSession(); // Xóa phiên đăng nhập
        System.out.println("Đăng xuất thành công!");
    }

    // Lưu phiên đăng nhập vào file
    private void saveSession() {
        try (FileWriter fw = new FileWriter(SESSION_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            // Lưu thông tin cần thiết của admin
            bw.write(String.valueOf(currentAdmin.getId()));
            bw.newLine();
            bw.write(currentAdmin.getUsername());
            bw.newLine();
            bw.write(currentAdmin.getPassword());
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu phiên đăng nhập: " + e.getMessage());
        }
    }

    // Tải phiên đăng nhập từ file
    private void loadSession() {
        File sessionFile = new File(SESSION_FILE);
        if (!sessionFile.exists()) {
            return; // Không có file session
        }

        try (FileReader fr = new FileReader(SESSION_FILE);
             BufferedReader br = new BufferedReader(fr)) {
            String idStr = br.readLine();
            String username = br.readLine();
            String password = br.readLine();

            // Kiểm tra xem phiên đăng nhập có hợp lệ không
            if (idStr != null && username != null && password != null) {
                int id = Integer.parseInt(idStr);

                // Kiểm tra xem phiên đăng nhập có khớp với admin mặc định không
                Admin defaultAdmin = Admin.getDefaultAdmin();
                if (defaultAdmin.getUsername().equals(username) &&
                        defaultAdmin.getPassword().equals(password)) {
                    currentAdmin = defaultAdmin;
                    System.out.println("Đã tải phiên đăng nhập của admin: " + username);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi đọc phiên đăng nhập: " + e.getMessage());
            // Xóa file session nếu bị lỗi
            clearSession();
        }
    }

    // Xóa phiên đăng nhập
    private void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            if (sessionFile.delete()) {
                System.out.println("Đã xóa phiên đăng nhập.");
            } else {
                System.err.println("Không thể xóa file phiên đăng nhập.");
            }
        }
    }
}