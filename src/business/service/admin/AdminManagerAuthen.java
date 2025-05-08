package business.service.admin;

import config.ColorPrintUtil;
import entity.Admin;
import validate.ValidateAdmin;

import java.io.*;
import java.util.Scanner;

public class AdminManagerAuthen {
    private static final String SESSION_FILE = "admin_session.txt";
    private Admin currentAdmin;

    public AdminManagerAuthen() {
        loadSession();
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
        if (hasActiveSession()) {
            ColorPrintUtil.printInfo("Bạn đã đăng nhập với tài khoản: " + currentAdmin.getUsername());
            return true;
        }
        Scanner sc = new Scanner(System.in);
        ColorPrintUtil.printHeader("ĐĂNG NHẬP ADMIN");
        boolean isValid = ValidateAdmin.validateLogin(sc);
        if (isValid) {
            // Tạo đối tượng admin mặc định
            currentAdmin = Admin.getDefaultAdmin();
            saveSession(); // Lưu phiên đăng nhập
            ColorPrintUtil.printSuccess("Đăng nhập thành công!");
            return true;
        } else {
            ColorPrintUtil.printError("Sai username hoặc password!");
            return false;
        }
    }

    // Đăng xuất admin
    public void logoutAdmin() {
        currentAdmin = null;
        clearSession(); // Xóa phiên đăng nhập
        ColorPrintUtil.printSuccess("Đăng xuất thành công!");
    }

    // Lưu phiên đăng nhập vào file
    private void saveSession() {
        try (FileWriter fw = new FileWriter(SESSION_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(currentAdmin.getUsername());
            bw.newLine();
            bw.write(currentAdmin.getPassword());
        } catch (IOException e) {
            ColorPrintUtil.printError("Lỗi khi lưu phiên đăng nhập: " + e.getMessage());
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
            String username = br.readLine();
            String password = br.readLine();
            // Kiểm tra xem phiên đăng nhập có hợp lệ không
            if (username != null && password != null) {
                // Kiểm tra xem phiên đăng nhập có khớp với admin mặc định không
                Admin defaultAdmin = Admin.getDefaultAdmin();
                if (defaultAdmin.getUsername().equals(username) &&
                        defaultAdmin.getPassword().equals(password)) {
                    currentAdmin = defaultAdmin;
                    ColorPrintUtil.printInfo("Đã tải phiên đăng nhập của admin: " + username);
                }
            }
        } catch (IOException | NumberFormatException e) {
            ColorPrintUtil.printError("Lỗi khi đọc phiên đăng nhập: " + e.getMessage());
            clearSession();
        }
    }

    // Xóa phiên đăng nhập
    private void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            if (sessionFile.delete()) {
                ColorPrintUtil.printInfo("Đã xóa phiên đăng nhập.");
            } else {
                ColorPrintUtil.printError("Không thể xóa file phiên đăng nhập.");
            }
        }
    }
}