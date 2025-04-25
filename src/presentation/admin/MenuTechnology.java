package presentation.admin;
// menu công nghe
import java.util.Scanner;

public class MenuTechnology {
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ CÔNG NGHỆ ===");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xoá công nghệ");
            System.out.println("5. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewTechnologies();
                    break;
                case 2:
                    addTechnology();
                    break;
                case 3:
                    editTechnology();
                    break;
                case 4:
                    deleteTechnology();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewTechnologies() {
        // Triển khai xem danh sách công nghệ
        System.out.println("Danh sách công nghệ...");
    }

    private void addTechnology() {
        System.out.print("Nhập tên công nghệ mới: ");
        String name = scanner.nextLine();
        // Thêm logic lưu công nghệ
        System.out.println("Đã thêm công nghệ: " + name);
    }

    private void editTechnology() {
        System.out.print("Nhập ID công nghệ cần sửa: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập tên mới: ");
        String newName = scanner.nextLine();
        // Thêm logic sửa công nghệ
        System.out.println("Đã cập nhật công nghệ ID " + id);
    }

    private void deleteTechnology() {
        System.out.print("Nhập ID công nghệ cần xóa: ");
        int id = scanner.nextInt();
        // Thêm logic xóa (đổi tên thành _deleted nếu có FK)
        System.out.println("Đã xóa (đổi tên) công nghệ ID " + id);
    }
}