package presentation.admin;

import java.util.Scanner;
// menu quan li don ung tuyen
public class MenuApplicationForm {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐƠN ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái");
            System.out.println("3. Hủy đơn");
            System.out.println("4. Xem chi tiết đơn");
            System.out.println("5. Gửi thông tin phỏng vấn");
            System.out.println("6. Cập nhật kết quả phỏng vấn");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewApplicationForms();
                    break;
                case 2:
                    filterByStatus();
                    break;
                case 3:
                    cancelApplication();
                    break;
                case 4:
                    viewApplicationDetail();
                    break;
                case 5:
                    sendInterviewInfo();
                    break;
                case 6:
                    updateInterviewResult();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewApplicationForms() {
        // Triển khai xem danh sách đơn
        System.out.println("Danh sách đơn ứng tuyển...");
    }

    private void filterByStatus() {
        System.out.println("Các trạng thái:");
        System.out.println("1. Pending");
        System.out.println("2. Handling");
        System.out.println("3. Interviewing");
        System.out.println("4. Done");
        System.out.print("Chọn trạng thái: ");
        int status = scanner.nextInt();
        // Thêm logic lọc
        System.out.println("Đã lọc đơn theo trạng thái " + status);
    }

    private void cancelApplication() {
        System.out.print("Nhập ID đơn cần hủy: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập lý do hủy: ");
        String reason = scanner.nextLine();
        // Thêm logic hủy đơn
        System.out.println("Đã hủy đơn ID " + id);
    }

    private void viewApplicationDetail() {
        System.out.print("Nhập ID đơn: ");
        int id = scanner.nextInt();
        // Thêm logic xem chi tiết và chuyển từ pending -> handling
        System.out.println("Chi tiết đơn ID " + id);
    }

    private void sendInterviewInfo() {
        System.out.print("Nhập ID đơn: ");
        int id = scanner.nextInt();
        // Thêm logic gửi thông tin phỏng vấn và chuyển sang interviewing
        System.out.println("Đã gửi thông tin phỏng vấn cho đơn ID " + id);
    }

    private void updateInterviewResult() {
        System.out.print("Nhập ID đơn: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập kết quả (Pass/Fail): ");
        String result = scanner.nextLine();
        System.out.print("Nhập ghi chú: ");
        String note = scanner.nextLine();
        // Thêm logic cập nhật kết quả
        System.out.println("Đã cập nhật kết quả cho đơn ID " + id);
    }
}