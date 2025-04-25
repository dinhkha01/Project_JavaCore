package presentation.candidate;

import java.util.Scanner;

public class MenuApplied {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐƠN ĐÃ ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn");
            System.out.println("3. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAppliedForms();
                    break;
                case 2:
                    viewFormDetail();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewAppliedForms() {
        // Triển khai xem danh sách đơn đã nộp
        System.out.println("Danh sách đơn đã nộp...");
    }

    private void viewFormDetail() {
        System.out.print("Nhập ID đơn: ");
        int formId = scanner.nextInt();
        scanner.nextLine();

        // Giả sử kiểm tra trạng thái đơn
        boolean isInterviewing = true; // Thay bằng logic thực tế
        boolean hasInterviewDate = true; // Thay bằng logic thực tế

        if (isInterviewing && hasInterviewDate) {
            System.out.println("1. Xác nhận tham gia phỏng vấn");
            System.out.println("2. Từ chối phỏng vấn");
            System.out.print("Chọn: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Đã xác nhận tham gia phỏng vấn");
            } else if (option == 2) {
                System.out.print("Nhập lý do từ chối: ");
                String reason = scanner.nextLine();
                System.out.println("Đã từ chối phỏng vấn với lý do: " + reason);
            }
        }

        System.out.println("Chi tiết đơn ID " + formId);
    }
}