package presentation.candidate;

import business.service.candidate.apply.ApplicationService;
import config.ColorPrintUtil;
import config.PrintColor;
import entity.Application;
import entity.RecruitmentPosition;

import java.util.List;
import java.util.Scanner;

public class MenuApplied {
    private Scanner scanner = new Scanner(System.in);
    private ApplicationService applicationService = new ApplicationService();
    private int candidateId;

    public MenuApplied(int candidateId) {
        this.candidateId = candidateId;
    }

    public void showMenu() {
        while (true) {
            ColorPrintUtil.printHeader("QUẢN LÝ ĐƠN ĐÃ ỨNG TUYỂN");
            ColorPrintUtil.printMenuItem(1, "Xem danh sách đơn đã nộp");
            ColorPrintUtil.printMenuItem(2, "Xem chi tiết đơn");
            ColorPrintUtil.printMenuItem(3, "Quay về menu chính");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            try {
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
                        ColorPrintUtil.printWarning("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập số!");
            }
        }
    }

    private void viewAppliedForms() {
        ColorPrintUtil.printHeader("DANH SÁCH ĐƠN ĐÃ NỘP");
        List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId);

        if (applications.isEmpty()) {
            ColorPrintUtil.printWarning("Bạn chưa nộp đơn ứng tuyển nào!");
            return;
        }

        ColorPrintUtil.printDivider();
        ColorPrintUtil.printTableHeader(String.format("%-5s| %-25s| %-15s| %-15s", "ID", "Vị trí", "Tiến độ", "Ngày nộp"));
        ColorPrintUtil.printDivider();

        for (Application app : applications) {
            RecruitmentPosition position = applicationService.getPositionById(app.getRecruitmentPositionId());
            String positionName = position != null ? position.getName() : "Unknown";

            String formattedDate = app.getCreatedAt() != null ?
                    app.getCreatedAt().toString().substring(0, 10) : "N/A";

            System.out.print(PrintColor.WHITE + String.format("%-5d| %-25s| ",
                    app.getId(),
                    truncateString(positionName, 25)));

            // Color-code progress status
            ColorPrintUtil.printStatus(app.getProgress());

            System.out.println(PrintColor.WHITE + String.format("| %-15s", formattedDate) + PrintColor.RESET);
        }
        ColorPrintUtil.printDivider();

        ColorPrintUtil.printInfo("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void viewFormDetail() {
        ColorPrintUtil.printInputField("\nNhập ID đơn muốn xem chi tiết");
        try {
            int formId = Integer.parseInt(scanner.nextLine());

            // Find the application in candidate's applications
            List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId);
            Application selectedApplication = null;

            for (Application app : applications) {
                if (app.getId() == formId) {
                    selectedApplication = app;
                    break;
                }
            }

            if (selectedApplication == null) {
                ColorPrintUtil.printError("Không tìm thấy đơn ứng tuyển với ID " + formId);
                return;
            }

            // Display application details
            RecruitmentPosition position = applicationService.getPositionById(selectedApplication.getRecruitmentPositionId());
            ColorPrintUtil.printHeader("CHI TIẾT ĐƠN ỨNG TUYỂN");

            ColorPrintUtil.printResultLabel("ID");
            System.out.println(selectedApplication.getId());

            ColorPrintUtil.printResultLabel("Vị trí");
            System.out.println(position != null ? position.getName() : "Unknown");

            ColorPrintUtil.printResultLabel("Tiến độ");
            ColorPrintUtil.printStatus(selectedApplication.getProgress());
            System.out.println();

            ColorPrintUtil.printResultLabel("CV URL");
            System.out.println(selectedApplication.getCvUrl());

            ColorPrintUtil.printResultLabel("Ngày tạo");
            System.out.println(selectedApplication.getCreatedAt());

            // Check if application is in interviewing state and has interview request date
            if ("interviewing".equalsIgnoreCase(selectedApplication.getProgress()) &&
                    selectedApplication.getInterviewRequestDate() != null) {

                ColorPrintUtil.printSubHeader("THÔNG TIN PHỎNG VẤN");
                ColorPrintUtil.printResultLabel("Ngày yêu cầu phỏng vấn");
                System.out.println(selectedApplication.getInterviewRequestDate());

                // Show interview time if available, regardless of response status
                if (selectedApplication.getInterviewTime() != null) {
                    ColorPrintUtil.printResultLabel("Thời gian phỏng vấn");
                    System.out.println(selectedApplication.getInterviewTime());
                }

                // Show interview link if available
                if (selectedApplication.getInterviewLink() != null) {
                    ColorPrintUtil.printResultLabel("Link phỏng vấn");
                    System.out.println(selectedApplication.getInterviewLink());
                }

                // Check if interview response exists
                if (selectedApplication.getInterviewRequestResult() != null &&
                        !selectedApplication.getInterviewRequestResult().isEmpty()) {
                    ColorPrintUtil.printResultLabel("Phản hồi phỏng vấn");
                    System.out.println(selectedApplication.getInterviewRequestResult());
                } else {
                    // If no response yet, ask for confirmation
                    ColorPrintUtil.printHighlight("\nBạn cần phản hồi yêu cầu phỏng vấn:");
                    ColorPrintUtil.printMenuItem(1, "Xác nhận tham gia phỏng vấn");
                    ColorPrintUtil.printMenuItem(2, "Từ chối phỏng vấn");
                    ColorPrintUtil.printPrompt("Chọn: ");

                    try {
                        int option = Integer.parseInt(scanner.nextLine());

                        if (option == 1) {
                            // Update application with confirmation
                            selectedApplication.setInterviewRequestResult("Đã xác nhận");
                            applicationService.updateInterviewResponse(selectedApplication.getId(), "Đã xác nhận", null);
                            ColorPrintUtil.printSuccess("Đã xác nhận tham gia phỏng vấn!");

                        } else if (option == 2) {
                            ColorPrintUtil.printInputField("Nhập lý do từ chối");
                            String reason = scanner.nextLine();

                            // Update application with rejection
                            selectedApplication.setInterviewRequestResult("Từ chối");
                            applicationService.updateInterviewResponse(selectedApplication.getId(), "Từ chối", reason);
                            ColorPrintUtil.printWarning("Đã từ chối phỏng vấn với lý do: " + reason);
                        } else {
                            ColorPrintUtil.printWarning("Lựa chọn không hợp lệ!");
                        }
                    } catch (NumberFormatException e) {
                        ColorPrintUtil.printError("Vui lòng nhập số!");
                    }
                }
            }

            // If the application is complete/done, show results
            if ("done".equalsIgnoreCase(selectedApplication.getProgress())) {
                ColorPrintUtil.printSubHeader("KẾT QUẢ TUYỂN DỤNG");

                ColorPrintUtil.printResultLabel("Kết quả");
                if (selectedApplication.getInterviewResult() != null) {
                    if (selectedApplication.getInterviewResult().equalsIgnoreCase("accepted")) {
                        System.out.println(PrintColor.GREEN_BOLD + selectedApplication.getInterviewResult() + PrintColor.RESET);
                    } else if (selectedApplication.getInterviewResult().equalsIgnoreCase("rejected")) {
                        System.out.println(PrintColor.RED_BOLD + selectedApplication.getInterviewResult() + PrintColor.RESET);
                    } else {
                        System.out.println(selectedApplication.getInterviewResult());
                    }
                } else {
                    ColorPrintUtil.printWarning("Chưa có kết quả");
                }

                if (selectedApplication.getInterviewResultNote() != null) {
                    ColorPrintUtil.printResultLabel("Ghi chú");
                    System.out.println(selectedApplication.getInterviewResultNote());
                }
            }

        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID đơn phải là số!");
        }

        ColorPrintUtil.printInfo("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
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