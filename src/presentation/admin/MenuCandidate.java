package presentation.admin;

import business.service.admin.CandidateService;
import config.ColorPrintUtil;
import config.PrintColor;
import entity.Candidate;

import java.util.List;
import java.util.Scanner;

public class MenuCandidate {
    private Scanner scanner = new Scanner(System.in);
    private CandidateService candidateService = new CandidateService();

    public void showMenu() {
        while (true) {
            ColorPrintUtil.printHeader("QUẢN LÝ ỨNG VIÊN");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/Mở khóa tài khoản");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm theo tên");
            System.out.println("5. Lọc theo tiêu chí");
            System.out.println("6. Quay về menu chính");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số hợp lệ!");
                continue;
            }

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
                    ColorPrintUtil.printWarning("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        if (candidates.isEmpty()) {
            ColorPrintUtil.printWarning("Không có ứng viên nào trong hệ thống.");
            return;
        }

        ColorPrintUtil.printHeader("DANH SÁCH ỨNG VIÊN");
        ColorPrintUtil.printTableHeader(String.format("%-5s %-25s %-25s %-15s %-10s %-10s %-10s",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái"));
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s  ",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender());

            // In trạng thái với màu khác nhau
            if ("active".equals(c.getStatus())) {

                System.out.printf(PrintColor.GREEN + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            } else {

                System.out.printf(PrintColor.RED + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            }
        }
    }

    private void toggleLockAccount() {
        ColorPrintUtil.printHeader("KHÓA/MỞ KHÓA TÀI KHOẢN");
        ColorPrintUtil.printPrompt("Nhập ID ứng viên: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
            return;
        }

        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            ColorPrintUtil.printError("Không tìm thấy ứng viên với ID: " + id);
            return;
        }

        ColorPrintUtil.printInfo("Ứng viên: " + candidate.getName());
        System.out.print("Trạng thái hiện tại: ");
        if ("active".equals(candidate.getStatus())) {
            System.out.println(PrintColor.GREEN + candidate.getStatus() + PrintColor.RESET);
        } else {
            System.out.println(PrintColor.RED + candidate.getStatus() + PrintColor.RESET);
        }

        ColorPrintUtil.printPrompt("Bạn có chắc muốn " +
                (candidate.getStatus().equals("active") ? "khóa" : "mở khóa") +
                " tài khoản này? (y/n): ");

        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            boolean result = candidateService.toggleCandidateStatus(id);
            if (result) {
                ColorPrintUtil.printSuccess("Đã thay đổi trạng thái tài khoản thành công!");
            } else {
                ColorPrintUtil.printError("Không thể thay đổi trạng thái tài khoản!");
            }
        } else {
            ColorPrintUtil.printInfo("Đã hủy thao tác.");
        }
    }

    private void resetPassword() {
        ColorPrintUtil.printHeader("RESET MẬT KHẨU");
        ColorPrintUtil.printPrompt("Nhập ID ứng viên: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
            return;
        }

        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            ColorPrintUtil.printError("Không tìm thấy ứng viên với ID: " + id);
            return;
        }

        ColorPrintUtil.printInfo("Ứng viên: " + candidate.getName());
        ColorPrintUtil.printPrompt("Bạn có chắc muốn reset mật khẩu cho tài khoản này? (y/n): ");

        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            String newPassword = candidateService.resetCandidatePassword(id);
            if (newPassword != null) {
                ColorPrintUtil.printSuccess("Đã reset mật khẩu thành công!");
                ColorPrintUtil.printHighlight("Mật khẩu mới: " + newPassword);
            } else {
                ColorPrintUtil.printError("Không thể reset mật khẩu!");
            }
        } else {
            ColorPrintUtil.printInfo("Đã hủy thao tác.");
        }
    }

    private void searchByName() {
        ColorPrintUtil.printHeader("TÌM KIẾM ỨNG VIÊN");
        ColorPrintUtil.printPrompt("Nhập tên cần tìm: ");
        String name = scanner.nextLine();

        List<Candidate> candidates = candidateService.searchCandidatesByName(name);

        if (candidates.isEmpty()) {
            ColorPrintUtil.printWarning("Không tìm thấy ứng viên nào phù hợp với từ khóa: " + name);
            return;
        }

        ColorPrintUtil.printHeader("KẾT QUẢ TÌM KIẾM CHO '" + name + "'");
        ColorPrintUtil.printTableHeader(String.format("%-5s %-25s %-25s %-15s %-10s %-10s %-10s",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái"));
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s ",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender());

            if ("active".equals(c.getStatus())) {
                System.out.printf(PrintColor.GREEN + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            } else {
                System.out.printf(PrintColor.RED + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            }
        }
    }

    private void filterCandidates() {
        ColorPrintUtil.printHeader("LỌC ỨNG VIÊN");
        ColorPrintUtil.printSubHeader("Lọc theo:");
        System.out.println("1. Kinh nghiệm");
        System.out.println("2. Tuổi");
        System.out.println("3. Giới tính");
        System.out.println("4. Công nghệ");
        ColorPrintUtil.printPrompt("Chọn tiêu chí: ");

        int criteria;
        try {
            criteria = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("Tiêu chí không hợp lệ!");
            return;
        }

        List<Candidate> candidates = null;

        switch (criteria) {
            case 1:
                ColorPrintUtil.printPrompt("Nhập kinh nghiệm tối thiểu (năm): ");
                int minExp = Integer.parseInt(scanner.nextLine());
                ColorPrintUtil.printPrompt("Nhập kinh nghiệm tối đa (năm): ");
                int maxExp = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByExperience(minExp, maxExp);
                break;
            case 2:
                ColorPrintUtil.printPrompt("Nhập tuổi tối thiểu: ");
                int minAge = Integer.parseInt(scanner.nextLine());
                ColorPrintUtil.printPrompt("Nhập tuổi tối đa: ");
                int maxAge = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByAge(minAge, maxAge);
                break;
            case 3:
                ColorPrintUtil.printPrompt("Nhập giới tính (Nam/Nữ): ");
                String gender = scanner.nextLine();
                candidates = candidateService.filterCandidatesByGender(gender);
                break;
            case 4:
                ColorPrintUtil.printPrompt("Nhập ID công nghệ: ");
                int techId = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByTechnology(techId);
                break;
            default:
                ColorPrintUtil.printError("Tiêu chí không hợp lệ!");
                return;
        }

        if (candidates == null || candidates.isEmpty()) {
            ColorPrintUtil.printWarning("Không tìm thấy ứng viên nào phù hợp với tiêu chí đã chọn.");
            return;
        }

        ColorPrintUtil.printHeader("KẾT QUẢ LỌC");
        ColorPrintUtil.printTableHeader(String.format("%-5s %-25s %-25s %-15s %-10s %-10s %-10s",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái"));
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s ",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender());

            if ("active".equals(c.getStatus())) {
                System.out.printf(PrintColor.GREEN + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            } else {
                System.out.printf(PrintColor.RED + "%-10s" + PrintColor.RESET + "\n", c.getStatus());
            }
        }
    }
}