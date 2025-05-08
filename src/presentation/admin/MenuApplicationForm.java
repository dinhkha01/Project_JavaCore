package presentation.admin;

import business.service.admin.ApplicationFormService;
import config.ColorPrintUtil;
import config.PrintColor;

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
            ColorPrintUtil.printHeader("QUẢN LÝ ĐƠN ỨNG TUYỂN");
            System.out.println(PrintColor.CYAN + "1. " + PrintColor.WHITE + "Xem danh sách đơn ứng tuyển" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "2. " + PrintColor.WHITE + "Lọc đơn theo trạng thái (progress)" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "3. " + PrintColor.WHITE + "Lọc đơn theo kết quả (result)" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "4. " + PrintColor.WHITE + "Hủy đơn" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "5. " + PrintColor.WHITE + "Xem chi tiết đơn" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "6. " + PrintColor.WHITE + "Gửi thông tin phỏng vấn" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "7. " + PrintColor.WHITE + "Cập nhật kết quả phỏng vấn" + PrintColor.RESET);
            System.out.println(PrintColor.CYAN + "8. " + PrintColor.WHITE + "Quay về menu chính" + PrintColor.RESET);
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ! Vui lòng nhập lại.");
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
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewApplicationForms() {
        ColorPrintUtil.printHeader("DANH SÁCH ĐƠN ỨNG TUYỂN");
        List<Object> applications = applicationService.getAllApplications();

        if (applications.isEmpty()) {
            ColorPrintUtil.printWarning("Không có đơn ứng tuyển nào!");
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void filterByProgress() {
        ColorPrintUtil.printHeader("LỌC THEO TRẠNG THÁI");
        System.out.println("Các trạng thái:");
        System.out.println(PrintColor.YELLOW + "1. Pending " + PrintColor.WHITE + "- Chờ xử lý" + PrintColor.RESET);
        System.out.println(PrintColor.BLUE + "2. Handling " + PrintColor.WHITE + "- Đang xử lý" + PrintColor.RESET);
        System.out.println(PrintColor.PURPLE + "3. Interviewing " + PrintColor.WHITE + "- Đang phỏng vấn" + PrintColor.RESET);
        System.out.println(PrintColor.GREEN + "4. Done " + PrintColor.WHITE + "- Hoàn thành" + PrintColor.RESET);

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            ColorPrintUtil.printPrompt("Chọn trạng thái (1-4): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 4) {
                    validInput = true;
                } else {
                    ColorPrintUtil.printError("Lựa chọn phải từ 1-4. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-4.");
            }
        }

        String progress;
        switch (choice) {
            case 1: progress = "pending"; break;
            case 2: progress = "handling"; break;
            case 3: progress = "interviewing"; break;
            case 4: progress = "done"; break;
            default:
                ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                return;
        }

        List<Map<String, Object>> applications = applicationService.filterByProgress(progress);

        if (applications.isEmpty()) {
            ColorPrintUtil.printWarning("Không tìm thấy đơn ứng tuyển nào với trạng thái: " + progress);
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void filterByResult() {
        ColorPrintUtil.printHeader("LỌC THEO KẾT QUẢ");
        System.out.println("Các kết quả:");
        System.out.println(PrintColor.GREEN + "1. Pass " + PrintColor.WHITE + "- Đậu" + PrintColor.RESET);
        System.out.println(PrintColor.RED + "2. Fail " + PrintColor.WHITE + "- Trượt" + PrintColor.RESET);

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            ColorPrintUtil.printPrompt("Chọn kết quả (1-2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 2) {
                    validInput = true;
                } else {
                    ColorPrintUtil.printError("Lựa chọn phải từ 1-2. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-2.");
            }
        }

        String result;
        switch (choice) {
            case 1: result = "Pass"; break;
            case 2: result = "Fail"; break;
            default:
                ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                return;
        }

        List<Map<String, Object>> applications = applicationService.filterByResult(result);

        if (applications.isEmpty()) {
            ColorPrintUtil.printWarning("Không tìm thấy đơn ứng tuyển nào với kết quả: " + result);
            return;
        }

        applicationService.displayApplicationList(applications);
    }

    private void cancelApplication() {
        ColorPrintUtil.printHeader("HỦY ĐƠN ỨNG TUYỂN");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            ColorPrintUtil.printPrompt("Nhập ID đơn cần hủy: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            ColorPrintUtil.printError("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        String reason = "";
        boolean validReason = false;

        while (!validReason) {
            ColorPrintUtil.printPrompt("Nhập lý do hủy: ");
            reason = scanner.nextLine();

            if (reason.trim().isEmpty()) {
                ColorPrintUtil.printError("Lý do hủy không được để trống! Vui lòng nhập lại.");
            } else {
                validReason = true;
            }
        }

        boolean success = applicationService.cancelApplication(id, reason);

        if (success) {
            ColorPrintUtil.printSuccess("Đã hủy đơn ứng tuyển thành công!");
        } else {
            ColorPrintUtil.printError("Hủy đơn ứng tuyển thất bại!");
        }
    }

    private void viewApplicationDetail() {
        ColorPrintUtil.printHeader("XEM CHI TIẾT ĐƠN ỨNG TUYỂN");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            ColorPrintUtil.printPrompt("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        applicationService.displayApplicationDetail(app);
    }

    private void sendInterviewInfo() {
        ColorPrintUtil.printHeader("GỬI THÔNG TIN PHỎNG VẤN");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            ColorPrintUtil.printPrompt("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            ColorPrintUtil.printError("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        // Kiểm tra trạng thái
        String progress = (String) app.get("progress");
        if ("interviewing".equals(progress) || "done".equals(progress)) {
            ColorPrintUtil.printWarning("Đơn đã ở trạng thái: " + progress + ", không thể gửi thông tin phỏng vấn!");
            return;
        }

        String interviewLink = "";
        boolean validLink = false;

        while(!validLink) {
            ColorPrintUtil.printPrompt("Nhập link phỏng vấn: ");
            interviewLink = scanner.nextLine();

            if (interviewLink.trim().isEmpty()) {
                ColorPrintUtil.printError("Link phỏng vấn không được để trống! Vui lòng nhập lại.");
            } else {
                validLink = true;
            }
        }

        Timestamp interviewTime = null;
        boolean validTime = false;

        while (!validTime) {
            ColorPrintUtil.printPrompt("Nhập thời gian phỏng vấn (định dạng dd/MM/yyyy HH:mm): ");
            String dateTimeStr = scanner.nextLine();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date parsedDate = dateFormat.parse(dateTimeStr);
                interviewTime = new Timestamp(parsedDate.getTime());
                validTime = true;
            } catch (ParseException e) {
                ColorPrintUtil.printError("Định dạng thời gian không hợp lệ! Vui lòng nhập lại.");
            }
        }

        boolean success = applicationService.sendInterviewInfo(id, interviewLink, interviewTime);

        if (success) {
            ColorPrintUtil.printSuccess("Đã gửi thông tin phỏng vấn thành công!");
        } else {
            ColorPrintUtil.printError("Gửi thông tin phỏng vấn thất bại!");
        }
    }

    private void updateInterviewResult() {
        ColorPrintUtil.printHeader("CẬP NHẬT KẾT QUẢ PHỎNG VẤN");

        int id = -1;
        boolean validId = false;

        while (!validId) {
            ColorPrintUtil.printPrompt("Nhập ID đơn: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                validId = true;
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("ID không hợp lệ! Vui lòng nhập số.");
            }
        }

        // Kiểm tra đơn tồn tại
        Map<String, Object> app = (Map<String, Object>) applicationService.viewApplicationDetail(id);
        if (app.isEmpty()) {
            ColorPrintUtil.printError("Không tìm thấy đơn ứng tuyển với ID: " + id);
            return;
        }

        // Kiểm tra trạng thái
        String progress = (String) app.get("progress");
        if (!"interviewing".equals(progress)) {
            ColorPrintUtil.printWarning("Đơn chưa ở trạng thái phỏng vấn (interviewing). Trạng thái hiện tại: " + progress);
            return;
        }

        ColorPrintUtil.printSubHeader("Chọn kết quả phỏng vấn");
        System.out.println(PrintColor.GREEN + "1. Pass " + PrintColor.WHITE + "- Đậu" + PrintColor.RESET);
        System.out.println(PrintColor.RED + "2. Fail " + PrintColor.WHITE + "- Trượt" + PrintColor.RESET);

        int resultChoice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            ColorPrintUtil.printPrompt("Lựa chọn của bạn (1-2): ");
            try {
                resultChoice = Integer.parseInt(scanner.nextLine());
                if (resultChoice >= 1 && resultChoice <= 2) {
                    validChoice = true;
                } else {
                    ColorPrintUtil.printError("Lựa chọn phải từ 1-2. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ! Vui lòng nhập số từ 1-2.");
            }
        }

        String result;
        switch (resultChoice) {
            case 1: result = "Pass"; break;
            case 2: result = "Fail"; break;
            default:
                ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                return;
        }

        ColorPrintUtil.printPrompt("Nhập ghi chú kết quả phỏng vấn: ");
        String note = scanner.nextLine();

        boolean success = applicationService.updateInterviewResult(id, result, note);

        if (success) {
            ColorPrintUtil.printSuccess("Đã cập nhật kết quả phỏng vấn thành công!");
        } else {
            ColorPrintUtil.printError("Cập nhật kết quả phỏng vấn thất bại!");
        }
    }
}