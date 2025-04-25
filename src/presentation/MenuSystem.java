package presentation;

import business.service.admin.AdminManager;
import presentation.admin.MenuAdmin;
import presentation.candidate.MenuCandidate;
import java.util.Scanner;

public class MenuSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminManager adminManager = new AdminManager();

        // Kiểm tra phiên đăng nhập trước khi hiển thị menu chính
        if (adminManager.hasActiveSession()) {
            // Nếu đã có phiên đăng nhập admin, chuyển thẳng đến menu admin
            MenuAdmin adminMenu = new MenuAdmin();
            adminMenu.showMainMenu();
        }

        while (true) {
            System.out.println("\n==================== HỆ THỐNG TUYỂN DỤNG ====================");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Đăng nhập Ứng viên");
            System.out.println("3. Đăng ký tài khoản Ứng viên");
            System.out.println("4. Thoát");
            System.out.println("============================================================");
            System.out.print("Nhập lựa chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    if (adminManager.loginAdmin()) {
                        MenuAdmin adminMenu = new MenuAdmin();
                        adminMenu.showMainMenu();
                    }
                    break;
                case 2:
                    // Giả sử đăng nhập thành công
                    MenuCandidate candidateMenu = new MenuCandidate();
                    candidateMenu.showMainMenu();
                    break;
                case 3:
                    registerCandidate();
                    break;
                case 4:
                    System.out.println("Đã thoát chương trình!");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void registerCandidate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập tên: ");
        String name = scanner.nextLine();
        System.out.print("Nhập email: ");
        String email = scanner.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String phone = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        // Thêm logic đăng ký
        System.out.println("Đăng ký thành công! Vui lòng đăng nhập.");
    }
}