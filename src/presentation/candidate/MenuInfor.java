package presentation.candidate;

import java.util.Scanner;

public class MenuInfor {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ THÔNG TIN CÁ NHÂN ===");
            System.out.println("1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());


            switch (choice) {
                case 1:
                    updatePersonalInfo();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void updatePersonalInfo() {
        System.out.println("Cập nhật thông tin cá nhân...");
        // Thêm logic cập nhật thông tin
        System.out.println("Đã cập nhật thông tin cá nhân");
    }

    private void changePassword() {
        System.out.print("Nhập mật khẩu cũ: ");
        String oldPass = scanner.nextLine();
        System.out.print("Nhập mật khẩu mới: ");
        String newPass = scanner.nextLine();
        // Thêm logic đổi mật khẩu với xác thực
        System.out.println("Đã đổi mật khẩu thành công");
    }
}