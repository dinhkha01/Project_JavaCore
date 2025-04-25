package presentation.admin;

import business.service.admin.TechnologyService;
import entity.Technology;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuTechnology {
    private Scanner scanner = new Scanner(System.in);
    private TechnologyService technologyService;

    public MenuTechnology() {
        try {
            this.technologyService = new TechnologyService();
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo TechnologyService: " + e.getMessage());
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ CÔNG NGHỆ ===");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xoá công nghệ");
            System.out.println("5. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số!");
                continue;
            }

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
        System.out.println("\n=== DANH SÁCH CÔNG NGHỆ ===");
        List<Technology> technologies = technologyService.getAllActiveTechnologies();

        if (technologies.isEmpty()) {
            System.out.println("Không có công nghệ nào trong hệ thống.");
            return;
        }

        System.out.println("ID\tTên công nghệ");
        System.out.println("---------------------------");
        for (Technology tech : technologies) {
            System.out.println(tech.getId() + "\t" + tech.getName());
        }
    }

    private void addTechnology() {
        System.out.println("\n=== THÊM CÔNG NGHỆ MỚI ===");
        System.out.print("Nhập tên công nghệ mới: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Tên công nghệ không được để trống!");
            return;
        }

        boolean result = technologyService.addTechnology(name);
        if (result) {
            System.out.println("Thêm công nghệ thành công: " + name);
        } else {
            System.out.println("Thêm công nghệ thất bại! Tên công nghệ đã tồn tại hoặc không hợp lệ.");
        }
    }

    private void editTechnology() {
        System.out.println("\n=== SỬA CÔNG NGHỆ ===");
        System.out.print("Nhập ID công nghệ cần sửa: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        Technology technology = technologyService.getTechnologyById(id);
        if (technology == null) {
            System.out.println("Không tìm thấy công nghệ với ID: " + id);
            return;
        }

        System.out.println("Công nghệ hiện tại: " + technology.getName());
        System.out.print("Nhập tên mới: ");
        String newName = scanner.nextLine().trim();

        if (newName.isEmpty()) {
            System.out.println("Tên công nghệ không được để trống!");
            return;
        }

        boolean result = technologyService.updateTechnology(id, newName);
        if (result) {
            System.out.println("Cập nhật công nghệ thành công!");
        } else {
            System.out.println("Cập nhật thất bại! Tên công nghệ đã tồn tại hoặc không hợp lệ.");
        }
    }

    private void deleteTechnology() {
        System.out.println("\n=== XÓA CÔNG NGHỆ ===");
        System.out.print("Nhập ID công nghệ cần xóa: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        Technology technology = technologyService.getTechnologyById(id);
        if (technology == null) {
            System.out.println("Không tìm thấy công nghệ với ID: " + id);
            return;
        }

        System.out.println("Bạn muốn xóa công nghệ: " + technology.getName());
        System.out.print("Xác nhận xóa? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean result = technologyService.deleteTechnology(id);
            if (result) {
                System.out.println("Xóa công nghệ thành công!");
            } else {
                System.out.println("Xóa công nghệ thất bại!");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }
}