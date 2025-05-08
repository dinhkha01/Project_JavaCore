
package business.service.admin;

import business.DAO.admin.applicationForm.ApplicationDaoImpl;
import business.DAO.admin.applicationForm.IApplication;
import config.PrintColor;
import config.ColorPrintUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ApplicationFormService {
    private final IApplication applicationDao;

    public ApplicationFormService() {
        this.applicationDao = new ApplicationDaoImpl();
    }

    // Lấy tất cả đơn ứng tuyển (trừ đơn đã hủy)
    public List<Object> getAllApplications() {
        return applicationDao.findAll();
    }

    // Lọc theo trạng thái
    public List<Map<String, Object>> filterByProgress(String progress) {
        return applicationDao.filterByProgress(progress);
    }

    // Lọc theo kết quả
    public List<Map<String, Object>> filterByResult(String result) {
        return applicationDao.filterByResult(result);
    }

    // Hủy đơn
    public boolean cancelApplication(int appId, String reason) {
        boolean result = applicationDao.cancelApplication(appId, reason);
        if (result) {
            ColorPrintUtil.printSuccess("Đã hủy đơn ứng tuyển thành công!");
        } else {
            ColorPrintUtil.printError("Không thể hủy đơn ứng tuyển!");
        }
        return result;
    }

    // Xem chi tiết đơn (tự động chuyển trạng thái từ pending -> handling)
    public Map<String, Object> viewApplicationDetail(int appId) {
        return (Map<String, Object>) applicationDao.findById(appId);
    }

    // Gửi thông tin phỏng vấn
    public boolean sendInterviewInfo(int appId, String interviewLink, Timestamp interviewTime) {
        boolean result = applicationDao.sendInterviewInfo(appId, interviewLink, interviewTime);
        if (result) {
            ColorPrintUtil.printSuccess("Đã gửi thông tin phỏng vấn thành công!");
        } else {
            ColorPrintUtil.printError("Không thể gửi thông tin phỏng vấn!");
        }
        return result;
    }

    // Cập nhật kết quả phỏng vấn
    public boolean updateInterviewResult(int appId, String result, String note) {
        boolean success = applicationDao.updateInterviewResult(appId, result, note);
        if (success) {
            ColorPrintUtil.printSuccess("Đã cập nhật kết quả phỏng vấn thành công!");
        } else {
            ColorPrintUtil.printError("Không thể cập nhật kết quả phỏng vấn!");
        }
        return success;
    }

    // Format hiển thị cho danh sách đơn ứng tuyển
    public void displayApplicationList(List<?> applications) {
        String tableBorder = "+-------+--------------------+--------------------+-------------+";
        ColorPrintUtil.printTableHeader(tableBorder);
        ColorPrintUtil.printTableHeader("| ID    | Ứng viên           | Vị trí             | Trạng thái  |");
        ColorPrintUtil.printTableHeader(tableBorder);

        for (Object obj : applications) {
            Map<String, Object> app = (Map<String, Object>) obj;
            System.out.print("| " + PrintColor.WHITE_BOLD + String.format("%-5d", app.get("id")) + PrintColor.RESET + " | ");
            System.out.print(PrintColor.WHITE + String.format("%-18s", limitLength(app.get("candidateName").toString(), 18)) + PrintColor.RESET + " | ");
            System.out.print(PrintColor.CYAN + String.format("%-18s", limitLength(app.get("positionName").toString(), 18)) + PrintColor.RESET + " | ");

            // Định dạng trạng thái với màu sắc phù hợp
            String progress = app.get("progress").toString();
            String formattedProgress = String.format("%-11s", progress);
            System.out.print(" ");
            ColorPrintUtil.printStatus(progress);

            // Thêm các khoảng trắng còn lại sau khi in status và đóng hàng
            int remainingSpaces = Math.max(0, 11 - progress.length());
            System.out.print(remainingSpaces > 0 ? " ".repeat(remainingSpaces) : "");
            System.out.println(" |");
        }

        ColorPrintUtil.printTableHeader(tableBorder);
    }

    // Format hiển thị chi tiết đơn ứng tuyển
    public void displayApplicationDetail(Map<String, Object> app) {
        if (app.isEmpty()) {
            ColorPrintUtil.printError("Không tìm thấy đơn ứng tuyển!");
            return;
        }

        ColorPrintUtil.printHeader("CHI TIẾT ĐƠN ỨNG TUYỂN");

        System.out.print(PrintColor.WHITE_BOLD + "ID: " + PrintColor.RESET);
        System.out.println(PrintColor.WHITE + app.get("id") + PrintColor.RESET);

        System.out.print(PrintColor.WHITE_BOLD + "Trạng thái: " + PrintColor.RESET);
        ColorPrintUtil.printStatus(app.get("progress").toString());
        System.out.println();

        ColorPrintUtil.printSubHeader("THÔNG TIN ỨNG VIÊN");
        System.out.println(PrintColor.WHITE_BOLD + "Tên: " + PrintColor.RESET + app.get("candidateName"));
        System.out.println(PrintColor.WHITE_BOLD + "Email: " + PrintColor.RESET + app.get("candidateEmail"));
        System.out.println(PrintColor.WHITE_BOLD + "Điện thoại: " + PrintColor.RESET + app.get("candidatePhone"));
        System.out.println(PrintColor.WHITE_BOLD + "Kinh nghiệm: " + PrintColor.RESET + app.get("candidateExperience") + " năm");
        System.out.println(PrintColor.WHITE_BOLD + "CV: " + PrintColor.RESET + app.get("cvUrl"));

        ColorPrintUtil.printSubHeader("THÔNG TIN VỊ TRÍ");
        System.out.println(PrintColor.WHITE_BOLD + "Vị trí: " + PrintColor.RESET + app.get("positionName"));
        System.out.println(PrintColor.WHITE_BOLD + "Mô tả: " + PrintColor.RESET + app.get("positionDescription"));
        System.out.println(PrintColor.WHITE_BOLD + "Lương: " + PrintColor.RESET + app.get("minSalary") + " - " + app.get("maxSalary"));
        System.out.println(PrintColor.WHITE_BOLD + "Yêu cầu kinh nghiệm: " + PrintColor.RESET + app.get("minExperience") + " năm");

        ColorPrintUtil.printSubHeader("THÔNG TIN PHỎNG VẤN");
        Timestamp interviewTime = (Timestamp) app.get("interviewTime");
        System.out.println(PrintColor.WHITE_BOLD + "Thời gian phỏng vấn: " + PrintColor.RESET +
                (interviewTime != null ? interviewTime : PrintColor.YELLOW + "Chưa xác định" + PrintColor.RESET));

        System.out.println(PrintColor.WHITE_BOLD + "Link phỏng vấn: " + PrintColor.RESET +
                (app.get("interviewLink") != null ? app.get("interviewLink") : PrintColor.YELLOW + "Chưa có" + PrintColor.RESET));

        String interviewResult = (String) app.get("interviewResult");
        System.out.print(PrintColor.WHITE_BOLD + "Kết quả: " + PrintColor.RESET);
        if (interviewResult != null) {
            if (interviewResult.equalsIgnoreCase("accepted")) {
                System.out.println(PrintColor.GREEN + interviewResult + PrintColor.RESET);
            } else if (interviewResult.equalsIgnoreCase("rejected")) {
                System.out.println(PrintColor.RED + interviewResult + PrintColor.RESET);
            } else {
                System.out.println(interviewResult);
            }
        } else {
            System.out.println(PrintColor.YELLOW + "Chưa có" + PrintColor.RESET);
        }

        System.out.println(PrintColor.WHITE_BOLD + "Ghi chú: " + PrintColor.RESET +
                (app.get("interviewResultNote") != null ? app.get("interviewResultNote") : "Không có"));

        System.out.println(PrintColor.CYAN_BOLD + "===================================" + PrintColor.RESET);
    }

    // Hàm giới hạn độ dài chuỗi (để hiển thị đẹp)
    private String limitLength(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}