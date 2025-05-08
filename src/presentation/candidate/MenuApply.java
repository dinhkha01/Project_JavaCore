package presentation.candidate;

import business.service.candidate.apply.ApplicationService;
import config.ColorPrintUtil;
import config.PrintColor;
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
            ColorPrintUtil.printHeader("XEM VÀ NỘP ĐƠN ỨNG TUYỂN");
            ColorPrintUtil.printMenuItem(1, "Xem danh sách vị trí đang hoạt động");
            ColorPrintUtil.printMenuItem(2, "Xem chi tiết và apply");
            ColorPrintUtil.printMenuItem(3, "Quản lý đơn ứng tuyển của tôi");
            ColorPrintUtil.printMenuItem(4, "Quay về menu chính");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

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
                        ColorPrintUtil.printWarning("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập số!");
            }
        }
    }

    private void viewActivePositions() {
        ColorPrintUtil.printHeader("DANH SÁCH VỊ TRÍ ĐANG HOẠT ĐỘNG");
        List<RecruitmentPosition> positions = applicationService.getAllActivePositions();

        if (positions.isEmpty()) {
            ColorPrintUtil.printWarning("Không có vị trí nào đang hoạt động!");
            return;
        }

        ColorPrintUtil.printDivider();
        ColorPrintUtil.printTableHeader(String.format("%-8s| %-25s| %-20s| %-10s| %s",
                "ID", "Tên vị trí", "Lương", "Kinh nghiệm", "Hạn nộp"));
        ColorPrintUtil.printDivider();

        for (RecruitmentPosition position : positions) {
            System.out.println(PrintColor.WHITE + String.format("%-8d| %-25s| %-20s| %-10d| %s",
                    position.getId(),
                    truncateString(position.getName(), 25),
                    position.getMinSalary() + " - " + position.getMaxSalary(),
                    position.getMinExperience(),
                    position.getExpiredDate()) + PrintColor.RESET);
        }
        ColorPrintUtil.printDivider();

        ColorPrintUtil.printInfo("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void viewDetailAndApply() {
        try {
            ColorPrintUtil.printInputField("\nNhập ID vị trí muốn xem chi tiết");
            int positionId = Integer.parseInt(scanner.nextLine());

            RecruitmentPosition position = applicationService.getPositionById(positionId);

            if (position == null) {
                ColorPrintUtil.printError("Không tìm thấy vị trí với ID " + positionId);
                return;
            }

            // Display position details with formatting
            ColorPrintUtil.printSubHeader("CHI TIẾT VỊ TRÍ TUYỂN DỤNG");
            System.out.println(applicationService.formatPositionDetails(position));

            // Ask if user wants to apply
            ColorPrintUtil.printPrompt("Bạn muốn apply vào vị trí này không? (Y/N): ");
            String applyChoice = scanner.nextLine();

            if (applyChoice.equalsIgnoreCase("Y")) {
                ColorPrintUtil.printInputField("Nhập URL CV của bạn");
                String cvUrl = scanner.nextLine();

                if (cvUrl.trim().isEmpty()) {
                    ColorPrintUtil.printError("URL CV không được để trống!");
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
                    ColorPrintUtil.printWarning("Bạn đã nộp đơn vào vị trí này rồi!");
                    return;
                }

                var application = applicationService.applyForPosition(currentCandidate.getId(), positionId, cvUrl);

                if (application != null) {
                    ColorPrintUtil.printOperationSuccess("NỘP ĐƠN THÀNH CÔNG");
                    ColorPrintUtil.printSuccess("Đã nộp đơn ứng tuyển thành công vào vị trí: " + position.getName());
                    ColorPrintUtil.printInfo("Bạn có thể theo dõi đơn trong mục 'Quản lý đơn ứng tuyển của tôi'");
                } else {
                    ColorPrintUtil.printOperationFailed("NỘP ĐƠN THẤT BẠI");
                    ColorPrintUtil.printError("Có lỗi xảy ra khi nộp đơn!");
                }
            }
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID vị trí phải là số!");
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