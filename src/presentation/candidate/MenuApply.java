package presentation.candidate;

import business.service.candidate.apply.ApplicationService;
import entity.Candidate;
import entity.RecruitmentPosition;

import java.util.List;
import java.util.Scanner;

public class MenuApply {
    private final Scanner scanner = new Scanner(System.in);
    private final ApplicationService applicationService = new ApplicationService();
    private final Candidate currentCandidate;
    private final MenuApplied menuApplied;

    public MenuApply(Candidate currentCandidate) {
        this.currentCandidate = currentCandidate;
        this.menuApplied = new MenuApplied(currentCandidate.getId());
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== XEM VÀ NỘP ĐƠN ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách vị trí đang hoạt động");
            System.out.println("2. Xem chi tiết và apply");
            System.out.println("3. Quản lý đơn ứng tuyển của tôi");
            System.out.println("4. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewActivePositions();
                        break;
                    case 2:
                        viewDetailAndApply();
                        break;
                    case 3:
                        menuApplied.showMenu(); // Use the MenuApplied for application management
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        }
    }

    private void viewActivePositions() {
        System.out.println("\n=== DANH SÁCH VỊ TRÍ ĐANG HOẠT ĐỘNG ===");
        List<RecruitmentPosition> positions = applicationService.getAllActivePositions();

        if (positions.isEmpty()) {
            System.out.println("Không có vị trí nào đang hoạt động!");
            return;
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-8s| %-25s| %-20s| %-10s| %s\n", "ID", "Tên vị trí", "Lương", "Kinh nghiệm", "Hạn nộp");
        System.out.println("----------------------------------------------------------------------");

        for (RecruitmentPosition position : positions) {
            System.out.printf("%-8d| %-25s| %-20s| %-10d| %s\n",
                    position.getId(),
                    truncateString(position.getName(), 25),
                    position.getMinSalary() + " - " + position.getMaxSalary(),
                    position.getMinExperience(),
                    position.getExpiredDate());
        }
        System.out.println("----------------------------------------------------------------------");

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void viewDetailAndApply() {
        try {
            System.out.print("\nNhập ID vị trí muốn xem chi tiết: ");
            int positionId = Integer.parseInt(scanner.nextLine());

            RecruitmentPosition position = applicationService.getPositionById(positionId);

            if (position == null) {
                System.out.println("Không tìm thấy vị trí với ID " + positionId);
                return;
            }

            // Display position details
            System.out.println(applicationService.formatPositionDetails(position));

            // Ask if user wants to apply
            System.out.print("Bạn muốn apply vào vị trí này không? (Y/N): ");
            String applyChoice = scanner.nextLine();

            if (applyChoice.equalsIgnoreCase("Y")) {
                System.out.print("Nhập URL CV của bạn: ");
                String cvUrl = scanner.nextLine();

                if (cvUrl.trim().isEmpty()) {
                    System.out.println("URL CV không được để trống!");
                    return;
                }

                // Check if user already applied for this position
                boolean alreadyApplied = false;
                for (var app : applicationService.getApplicationsByCandidateId(currentCandidate.getId())) {
                    if (app.getRecruitmentPositionId() == positionId && app.getDestroyAt() == null) {
                        alreadyApplied = true;
                        break;
                    }
                }

                if (alreadyApplied) {
                    System.out.println("Bạn đã nộp đơn vào vị trí này rồi!");
                    return;
                }

                var application = applicationService.applyForPosition(currentCandidate.getId(), positionId, cvUrl);

                if (application != null) {
                    System.out.println("Đã nộp đơn ứng tuyển thành công vào vị trí: " + position.getName());
                    System.out.println("Bạn có thể theo dõi đơn trong mục 'Quản lý đơn ứng tuyển của tôi'");
                } else {
                    System.out.println("Có lỗi xảy ra khi nộp đơn!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID vị trí phải là số!");
        }
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= maxLength) {
            return str;
        }

        return str.substring(0, maxLength - 3) + "...";
    }
}