package presentation.admin;

import business.service.admin.CandidateService;
import entity.Candidate;

import java.util.List;
import java.util.Scanner;

public class MenuCandidate {
    private Scanner scanner = new Scanner(System.in);
    private CandidateService candidateService = new CandidateService();

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
        List<Candidate> candidates = candidateService.getAllCandidates();
        if (candidates.isEmpty()) {
            System.out.println("Không có ứng viên nào trong hệ thống.");
            return;
        }

        System.out.println("\n=== DANH SÁCH ỨNG VIÊN ===");
        System.out.printf("%-5s %-25s %-25s %-15s %-10s %-10s %-10s\n",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s %-10s\n",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender(), c.getStatus());
        }
    }

    private void toggleLockAccount() {
        System.out.print("Nhập ID ứng viên: ");
        int id = Integer.parseInt(scanner.nextLine());

        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên với ID: " + id);
            return;
        }

        System.out.println("Ứng viên: " + candidate.getName());
        System.out.println("Trạng thái hiện tại: " + candidate.getStatus());
        System.out.print("Bạn có chắc muốn " +
                (candidate.getStatus().equals("active") ? "khóa" : "mở khóa") +
                " tài khoản này? (y/n): ");

        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            boolean result = candidateService.toggleCandidateStatus(id);
            if (result) {
                System.out.println("Đã thay đổi trạng thái tài khoản thành công!");
            } else {
                System.out.println("Không thể thay đổi trạng thái tài khoản!");
            }
        }
    }

    private void resetPassword() {
        System.out.print("Nhập ID ứng viên: ");
        int id = Integer.parseInt(scanner.nextLine());

        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            System.out.println("Không tìm thấy ứng viên với ID: " + id);
            return;
        }

        System.out.println("Ứng viên: " + candidate.getName());
        System.out.print("Bạn có chắc muốn reset mật khẩu cho tài khoản này? (y/n): ");

        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            String newPassword = candidateService.resetCandidatePassword(id);
            if (newPassword != null) {
                System.out.println("Đã reset mật khẩu thành công!");
                System.out.println("Mật khẩu mới: " + newPassword);
            } else {
                System.out.println("Không thể reset mật khẩu!");
            }
        }
    }

    private void searchByName() {
        System.out.print("Nhập tên cần tìm: ");
        String name = scanner.nextLine();

        List<Candidate> candidates = candidateService.searchCandidatesByName(name);

        if (candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên nào phù hợp với từ khóa: " + name);
            return;
        }

        System.out.println("\n=== KẾT QUẢ TÌM KIẾM CHO '" + name + "' ===");
        System.out.printf("%-5s %-25s %-25s %-15s %-10s %-10s %-10s\n",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s %-10s\n",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender(), c.getStatus());
        }
    }

    private void filterCandidates() {
        System.out.println("Lọc theo:");
        System.out.println("1. Kinh nghiệm");
        System.out.println("2. Tuổi");
        System.out.println("3. Giới tính");
        System.out.println("4. Công nghệ");
        System.out.print("Chọn tiêu chí: ");
        int criteria = Integer.parseInt(scanner.nextLine());

        List<Candidate> candidates = null;

        switch (criteria) {
            case 1:
                System.out.print("Nhập kinh nghiệm tối thiểu (năm): ");
                int minExp = Integer.parseInt(scanner.nextLine());
                System.out.print("Nhập kinh nghiệm tối đa (năm): ");
                int maxExp = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByExperience(minExp, maxExp);
                break;
            case 2:
                System.out.print("Nhập tuổi tối thiểu: ");
                int minAge = Integer.parseInt(scanner.nextLine());
                System.out.print("Nhập tuổi tối đa: ");
                int maxAge = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByAge(minAge, maxAge);
                break;
            case 3:
                System.out.print("Nhập giới tính (Nam/Nữ): ");
                String gender = scanner.nextLine();
                candidates = candidateService.filterCandidatesByGender(gender);
                break;
            case 4:
                System.out.print("Nhập ID công nghệ: ");
                int techId = Integer.parseInt(scanner.nextLine());
                candidates = candidateService.filterCandidatesByTechnology(techId);
                break;
            default:
                System.out.println("Tiêu chí không hợp lệ!");
                return;
        }

        if (candidates == null || candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên nào phù hợp với tiêu chí đã chọn.");
            return;
        }

        System.out.println("\n=== KẾT QUẢ LỌC ===");
        System.out.printf("%-5s %-25s %-25s %-15s %-10s %-10s %-10s\n",
                "ID", "Tên", "Email", "SĐT", "KN (năm)", "Giới tính", "Trạng thái");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Candidate c : candidates) {
            System.out.printf("%-5d %-25s %-25s %-15s %-10d %-10s %-10s\n",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getExperience(), c.getGender(), c.getStatus());
        }
    }

    private String generateRandomPassword() {
        // Tạo mật khẩu ngẫu nhiên
        return "Temp@" + (int)(Math.random() * 10000);
    }
}