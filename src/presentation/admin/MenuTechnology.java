package presentation.admin;

import business.service.admin.TechnologyService;
import config.ColorPrintUtil;
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
            ColorPrintUtil.printError("Lỗi khởi tạo TechnologyService: " + e.getMessage());
        }
    }

    public void showMenu() {
        while (true) {
            ColorPrintUtil.printHeader("QUẢN LÝ CÔNG NGHỆ");
            System.out.println("1. Xem danh sách công nghệ");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xoá công nghệ");
            System.out.println("5. Quay về menu chính");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số!");
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
                    ColorPrintUtil.printWarning("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewTechnologies() {
        ColorPrintUtil.printHeader("DANH SÁCH CÔNG NGHỆ");
        List<Technology> technologies = technologyService.getAllActiveTechnologies();

        if (technologies.isEmpty()) {
            ColorPrintUtil.printWarning("Không có công nghệ nào trong hệ thống.");
            return;
        }

        ColorPrintUtil.printTableHeader("ID\tTên công nghệ");
        System.out.println("---------------------------");
        for (Technology tech : technologies) {
            System.out.println(tech.getId() + "\t" + tech.getName());
        }
    }

    private void addTechnology() {
        ColorPrintUtil.printHeader("THÊM CÔNG NGHỆ MỚI");
        ColorPrintUtil.printPrompt("Nhập tên công nghệ mới: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            ColorPrintUtil.printError("Tên công nghệ không được để trống!");
            return;
        }

        boolean result = technologyService.addTechnology(name);
        if (result) {
            ColorPrintUtil.printSuccess("Thêm công nghệ thành công: " + name);
        } else {
            ColorPrintUtil.printError("Thêm công nghệ thất bại! Tên công nghệ đã tồn tại hoặc không hợp lệ.");
        }
    }

    private void editTechnology() {
        ColorPrintUtil.printHeader("SỬA CÔNG NGHỆ");
        ColorPrintUtil.printPrompt("Nhập ID công nghệ cần sửa: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
            return;
        }

        Technology technology = technologyService.getTechnologyById(id);
        if (technology == null) {
            ColorPrintUtil.printError("Không tìm thấy công nghệ với ID: " + id);
            return;
        }

        ColorPrintUtil.printInfo("Công nghệ hiện tại: " + technology.getName());
        ColorPrintUtil.printPrompt("Nhập tên mới: ");
        String newName = scanner.nextLine().trim();

        if (newName.isEmpty()) {
            ColorPrintUtil.printError("Tên công nghệ không được để trống!");
            return;
        }

        boolean result = technologyService.updateTechnology(id, newName);
        if (result) {
            ColorPrintUtil.printSuccess("Cập nhật công nghệ thành công!");
        } else {
            ColorPrintUtil.printError("Cập nhật thất bại! Tên công nghệ đã tồn tại hoặc không hợp lệ.");
        }
    }

    private void deleteTechnology() {
        ColorPrintUtil.printHeader("XÓA CÔNG NGHỆ");
        ColorPrintUtil.printPrompt("Nhập ID công nghệ cần xóa: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
            return;
        }

        Technology technology = technologyService.getTechnologyById(id);
        if (technology == null) {
            ColorPrintUtil.printError("Không tìm thấy công nghệ với ID: " + id);
            return;
        }

        ColorPrintUtil.printWarning("Bạn muốn xóa công nghệ: " + technology.getName());
        ColorPrintUtil.printPrompt("Xác nhận xóa? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean result = technologyService.deleteTechnology(id);
            if (result) {
                ColorPrintUtil.printSuccess("Xóa công nghệ thành công!");
            } else {
                ColorPrintUtil.printError("Xóa công nghệ thất bại!");
            }
        } else {
            ColorPrintUtil.printInfo("Đã hủy thao tác xóa.");
        }
    }
}