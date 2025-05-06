package presentation.candidate;

import business.service.candidate.apply.ApplicationService;
import entity.Application;
import entity.RecruitmentPosition;

import java.sql.Timestamp;
import java.util.Date;
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
            System.out.println("\n=== QUẢN LÝ ĐƠN ĐÃ ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách đơn đã nộp");
            System.out.println("2. Xem chi tiết đơn");
            System.out.println("3. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

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
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        }
    }

    private void viewAppliedForms() {
        System.out.println("\n=== DANH SÁCH ĐƠN ĐÃ NỘP ===");
        List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId);

        if (applications.isEmpty()) {
            System.out.println("Bạn chưa nộp đơn ứng tuyển nào!");
            return;
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-5s| %-25s| %-15s| %-15s\n", "ID", "Vị trí", "Tiến độ", "Ngày nộp");
        System.out.println("-------------------------------------------------------------");

        for (Application app : applications) {
            RecruitmentPosition position = applicationService.getPositionById(app.getRecruitmentPositionId());
            String positionName = position != null ? position.getName() : "Unknown";

            String formattedDate = app.getCreatedAt() != null ?
                    app.getCreatedAt().toString().substring(0, 10) : "N/A";

            System.out.printf("%-5d| %-25s| %-15s| %-15s\n",
                    app.getId(),
                    truncateString(positionName, 25),
                    app.getProgress(),
                    formattedDate);
        }
        System.out.println("-------------------------------------------------------------");

        System.out.println("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    private void viewFormDetail() {
        System.out.print("\nNhập ID đơn muốn xem chi tiết: ");
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
                System.out.println("Không tìm thấy đơn ứng tuyển với ID " + formId);
                return;
            }

            // Display application details
            RecruitmentPosition position = applicationService.getPositionById(selectedApplication.getRecruitmentPositionId());
            System.out.println("\n=== CHI TIẾT ĐƠN ỨNG TUYỂN ===");
            System.out.println("ID: " + selectedApplication.getId());
            System.out.println("Vị trí: " + (position != null ? position.getName() : "Unknown"));
            System.out.println("Tiến độ: " + selectedApplication.getProgress());
            System.out.println("CV URL: " + selectedApplication.getCvUrl());
            System.out.println("Ngày tạo: " + selectedApplication.getCreatedAt());

            // Check if application is in interviewing state and has interview request date
            if ("interviewing".equalsIgnoreCase(selectedApplication.getProgress()) &&
                    selectedApplication.getInterviewRequestDate() != null) {

                System.out.println("\n--- THÔNG TIN PHỎNG VẤN ---");
                System.out.println("Ngày yêu cầu phỏng vấn: " + selectedApplication.getInterviewRequestDate());

                // Show interview time if available, regardless of response status
                if (selectedApplication.getInterviewTime() != null) {
                    System.out.println("Thời gian phỏng vấn: " + selectedApplication.getInterviewTime());
                }

                // Show interview link if available
                if (selectedApplication.getInterviewLink() != null) {
                    System.out.println("Link phỏng vấn: " + selectedApplication.getInterviewLink());
                }

                // Check if interview response exists
                if (selectedApplication.getInterviewRequestResult() != null &&
                        !selectedApplication.getInterviewRequestResult().isEmpty()) {
                    System.out.println("Phản hồi phỏng vấn: " + selectedApplication.getInterviewRequestResult());
                } else {
                    // If no response yet, ask for confirmation
                    System.out.println("\nBạn cần phản hồi yêu cầu phỏng vấn:");
                    System.out.println("1. Xác nhận tham gia phỏng vấn");
                    System.out.println("2. Từ chối phỏng vấn");
                    System.out.print("Chọn: ");

                    try {
                        int option = Integer.parseInt(scanner.nextLine());

                        if (option == 1) {
                            // Update application with confirmation
                            selectedApplication.setInterviewRequestResult("Đã xác nhận");
                            applicationService.updateInterviewResponse(selectedApplication.getId(), "Đã xác nhận", null);
                            System.out.println("Đã xác nhận tham gia phỏng vấn!");

                        } else if (option == 2) {
                            System.out.print("Nhập lý do từ chối: ");
                            String reason = scanner.nextLine();

                            // Update application with rejection
                            selectedApplication.setInterviewRequestResult("Từ chối");
                            applicationService.updateInterviewResponse(selectedApplication.getId(), "Từ chối", reason);
                            System.out.println("Đã từ chối phỏng vấn với lý do: " + reason);
                        } else {
                            System.out.println("Lựa chọn không hợp lệ!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập số!");
                    }
                }
            }

            // If the application is complete/done, show results
            if ("done".equalsIgnoreCase(selectedApplication.getProgress())) {
                System.out.println("\n--- KẾT QUẢ TUYỂN DỤNG ---");
                System.out.println("Kết quả: " + (selectedApplication.getInterviewResult() != null ?
                        selectedApplication.getInterviewResult() : "Chưa có kết quả"));
                if (selectedApplication.getInterviewResultNote() != null) {
                    System.out.println("Ghi chú: " + selectedApplication.getInterviewResultNote());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("ID đơn phải là số!");
        }

        System.out.println("\nNhấn Enter để tiếp tục...");
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