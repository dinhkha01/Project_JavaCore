package presentation.admin;

import business.service.admin.ApplicationFormService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuApplicationForm {
    private Scanner scanner = new Scanner(System.in);
    private ApplicationFormService applicationService = new ApplicationFormService();

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐƠN ỨNG TUYỂN ===");
            System.out.println("1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái (progress)");
            System.out.println("3. Lọc đơn theo kết quả (result)");
            System.out.println("4. Hủy đơn");
            System.out.println("5. Xem chi tiết đơn");
            System.out.println("6. Gửi thông tin phỏng vấn");
            System.out.println("7. Cập nhật kết quả phỏng vấn");
            System.out.println("8. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập lại.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewApplicationForms();
                    break;
                case 2:
                    filterByProgress();
                    break;
                case 3:
                    filterByResult();
                    break;
                case 4:
                    cancelApplication();
                    break;
                case 5:
                    viewApplicationDetail();
                    break;
                case 6:
                    sendInterviewInfo();
                    break;
                case 7:
                    updateInterviewResult();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewApplicationForms() {
        System.out.println("\n=== DANH SÁCH ĐƠN ỨNG TUYỂN ===");
        List<Object> applications = applicationService.getAllApplications();

        if (applications.isEmpty()) {
            System.out.println("Không có đơn ứng tuyển nào!");
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void filterByProgress() {
        System.out.println("\n=== LỌC THEO TRẠNG THÁI ===");
        System.out.println("Các trạng thái:");
        System.out.println("1. Pending - Chờ xử lý");
        System.out.println("2. Handling - Đang xử lý");
        System.out.println("3. Interviewing - Đang phỏng vấn");
        System.out.println("4. Done - Hoàn thành");

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Chọn trạng thái (1-4): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 4) {
                    validInput = true;
                } else {
                    System.out.println("Lựa chọn phải từ 1-4. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-4.");
            }
        }

        String progress;
        switch (choice) {
            case 1: progress = "pending"; break;
            case 2: progress = "handling"; break;
            case 3: progress = "interviewing"; break;
            case 4: progress = "done"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        List<Map<String, Object>> applications = applicationService.filterByProgress(progress);

        if (applications.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển nào với trạng thái: " + progress);
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void filterByResult() {
        System.out.println("\n=== LỌC THEO KẾT QUẢ ===");
        System.out.println("Các kết quả:");
        System.out.println("1. Pass - Đậu");
        System.out.println("2. Fail - Trượt");

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Chọn kết quả (1-2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 2) {
                    validInput = true;
                } else {
                    System.out.println("Lựa chọn phải từ 1-2. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-2.");
            }
        }

        String result;
        switch (choice) {
            case 1: result = "Pass"; break;
            case 2: result = "Fail"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        List<Map<String, Object>> applications = applicationService.filterByResult(result);

        if (applications.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển nào với kết quả: " + result);
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void cancelApplication() {
        System.out.println("\n=== HỦY ĐƠN ỨNG TUYỂN ===");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            System.out.print("Nhập ID đơn cần hủy: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        String reason = "";
        boolean validReason = false;

        while (!validReason) {
            System.out.print("Nhập lý do hủy: ");
            reason = scanner.nextLine();

            if (reason.trim().isEmpty()) {
                System.out.println("Lý do hủy không được để trống! Vui lòng nhập lại.");
            } else {
                validReason = true;
            }
        }

        boolean success = applicationService.cancelApplication(id, reason);

        if (success) {
            System.out.println("Đã hủy đơn ứng tuyển thành công!");
        } else {
            System.out.println("Hủy đơn ứng tuyển thất bại!");
        }
    }

    private void viewApplicationDetail() {
        System.out.println("\n=== XEM CHI TIẾT ĐƠN ỨNG TUYỂN ===");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            System.out.print("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        applicationService.displayApplicationDetail(app);
    }

    private void sendInterviewInfo() {
        System.out.println("\n=== GỬI THÔNG TIN PHỎNG VẤN ===");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            System.out.print("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        // Kiểm tra trạng thái
        String progress = (String) app.get("progress");
        if ("interviewing".equals(progress) || "done".equals(progress)) {
            System.out.println("Đơn đã ở trạng thái: " + progress + ", không thể gửi thông tin phỏng vấn!");
            return;
        }

        String interviewLink = "";
        boolean validLink = false;

        while(!validLink) {
            System.out.print("Nhập link phỏng vấn: ");
            interviewLink = scanner.nextLine();

            if (interviewLink.trim().isEmpty()) {
                System.out.println("Link phỏng vấn không được để trống! Vui lòng nhập lại.");
            } else {
                validLink = true;
            }
        }

        Timestamp interviewTime = null;
        boolean validTime = false;

        while (!validTime) {
            System.out.println("Nhập thời gian phỏng vấn (định dạng dd/MM/yyyy HH:mm): ");
            String dateTimeStr = scanner.nextLine();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date parsedDate = dateFormat.parse(dateTimeStr);
                interviewTime = new Timestamp(parsedDate.getTime());
                validTime = true;
            } catch (ParseException e) {
                System.out.println("Định dạng thời gian không hợp lệ! Vui lòng nhập lại.");
            }
        }

        boolean success = applicationService.sendInterviewInfo(id, interviewLink, interviewTime);

        if (success) {
            System.out.println("Đã gửi thông tin phỏng vấn thành công!");
        } else {
            System.out.println("Gửi thông tin phỏng vấn thất bại!");
        }
    }

    private void updateInterviewResult() {
        System.out.println("\n=== CẬP NHẬT KẾT QUẢ PHỎNG VẤN ===");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            System.out.print("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        // Kiểm tra trạng thái
        String progress = (String) app.get("progress");
        if (!"interviewing".equals(progress)) {
            System.out.println("Đơn chưa ở trạng thái phỏng vấn (interviewing). Trạng thái hiện tại: " + progress);
            return;
        }

        System.out.println("Chọn kết quả phỏng vấn:");
        System.out.println("1. Pass - Đậu");
        System.out.println("2. Fail - Trượt");

        int resultChoice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Lựa chọn của bạn (1-2): ");
            try {
                resultChoice = Integer.parseInt(scanner.nextLine());
                if (resultChoice >= 1 && resultChoice <= 2) {
                    validChoice = true;
                } else {
                    System.out.println("Lựa chọn phải từ 1-2. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-2.");
            }
        }

        String result;
        switch (resultChoice) {
            case 1: result = "Pass"; break;
            case 2: result = "Fail"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        System.out.print("Nhập ghi chú kết quả phỏng vấn: ");
        String note = scanner.nextLine();

        boolean success = applicationService.updateInterviewResult(id, result, note);

        if (success) {
            System.out.println("Đã cập nhật kết quả phỏng vấn thành công!");
        } else {
            System.out.println("Cập nhật kết quả phỏng vấn thất bại!");
        }
    }
}