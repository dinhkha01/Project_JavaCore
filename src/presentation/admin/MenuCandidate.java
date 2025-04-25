package presentation.admin;

import java.util.Scanner;
// menu ung vien
public class MenuCandidate {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ỨNG VIÊN ===");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/Mở khóa tài khoản");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm theo tên");
            System.out.println("5. Lọc theo tiêu chí");
            System.out.println("6. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewCandidates();
                    break;
                case 2:
                    toggleLockAccount();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    searchByName();
                    break;
                case 5:
                    filterCandidates();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewCandidates() {
        // Triển khai xem danh sách ứng viên
        System.out.println("Danh sách ứng viên...");
    }

    private void toggleLockAccount() {
        System.out.print("Nhập ID ứng viên: ");
        int id = scanner.nextInt();
        // Thêm logic khóa/mở khóa
        System.out.println("Đã thay đổi trạng thái tài khoản ứng viên ID " + id);
    }

    private void resetPassword() {
        System.out.print("Nhập ID ứng viên: ");
        int id = scanner.nextInt();
        String newPassword = generateRandomPassword();
        // Thêm logic reset mật khẩu
        System.out.println("Mật khẩu mới: " + newPassword);
    }

    private String generateRandomPassword() {
        // Tạo mật khẩu ngẫu nhiên
        return "Temp@" + (int)(Math.random() * 10000);
    }

    private void searchByName() {
        System.out.print("Nhập tên cần tìm: ");
        String name = scanner.nextLine();
        // Thêm logic tìm kiếm
        System.out.println("Kết quả tìm kiếm cho '" + name + "'");
    }

    private void filterCandidates() {
        System.out.println("Lọc theo:");
        System.out.println("1. Kinh nghiệm");
        System.out.println("2. Tuổi");
        System.out.println("3. Giới tính");
        System.out.println("4. Công nghệ");
        System.out.print("Chọn tiêu chí: ");
        int criteria = scanner.nextInt();
        scanner.nextLine();
        // Thêm logic lọc
        System.out.println("Đã lọc ứng viên theo tiêu chí " + criteria);
    }
}