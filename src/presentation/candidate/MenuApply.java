package presentation.candidate;

import java.util.Scanner;

public class MenuApply {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== XEM VÀ NỘP ĐƠN ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và apply");
            System.out.println("3. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewActivePositions();
                    break;
                case 2:
                    viewDetailAndApply();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewActivePositions() {
        // Triển khai xem danh sách vị trí
        System.out.println("Danh sách vị trí đang hoạt động...");
    }

    private void viewDetailAndApply() {
        System.out.print("Nhập ID vị trí: ");
        int positionId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập URL CV: ");
        String cvUrl = scanner.nextLine();
        // Thêm logic apply
        System.out.println("Đã nộp đơn ứng tuyển vị trí ID " + positionId);
    }
}