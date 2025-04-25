package presentation.admin;

import java.util.Scanner;
// menu quan li tuyen dung
public class MenuRecruitmentPosition {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG ===");
            System.out.println("1. Thêm vị trí tuyển dụng mới");
            System.out.println("2. Cập nhật vị trí tuyển dụng");
            System.out.println("3. Xóa vị trí");
            System.out.println("4. Xem danh sách vị trí đang hoạt động");
            System.out.println("5. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addPosition();
                    break;
                case 2:
                    updatePosition();
                    break;
                case 3:
                    deletePosition();
                    break;
                case 4:
                    viewActivePositions();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void addPosition() {
        System.out.print("Nhập tên vị trí: ");
        String name = scanner.nextLine();
        // Thêm logic lưu vị trí
        System.out.println("Đã thêm vị trí: " + name);
    }

    private void updatePosition() {
        System.out.print("Nhập ID vị trí cần cập nhật: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập tên mới: ");
        String newName = scanner.nextLine();
        // Thêm logic cập nhật
        System.out.println("Đã cập nhật vị trí ID " + id);
    }

    private void deletePosition() {
        System.out.print("Nhập ID vị trí cần xóa: ");
        int id = scanner.nextInt();
        // Thêm logic xóa (đổi tên thành _deleted nếu có FK)
        System.out.println("Đã xóa (đổi tên) vị trí ID " + id);
    }

    private void viewActivePositions() {
        // Triển khai xem danh sách vị trí
        System.out.println("Danh sách vị trí đang hoạt động...");
    }
}