package business.service.admin;

import business.dao.admin.applicationForm.ApplicationDaoImpl;
import business.dao.admin.applicationForm.IApplication;

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
        return applicationDao.cancelApplication(appId, reason);
    }

    // Xem chi tiết đơn (tự động chuyển trạng thái từ pending -> handling)
    public Map<String, Object> viewApplicationDetail(int appId) {
        return (Map<String, Object>) applicationDao.findById(appId);
    }

    // Gửi thông tin phỏng vấn
    public boolean sendInterviewInfo(int appId, String interviewLink, Timestamp interviewTime) {
        return applicationDao.sendInterviewInfo(appId, interviewLink, interviewTime);
    }

    // Cập nhật kết quả phỏng vấn
    public boolean updateInterviewResult(int appId, String result, String note) {
        return applicationDao.updateInterviewResult(appId, result, note);
    }

    // Format hiển thị cho danh sách đơn ứng tuyển
    public void displayApplicationList(List<?> applications) {
        System.out.println("+-------+--------------------+--------------------+-------------+");
        System.out.println("| ID    | Ứng viên           | Vị trí             | Trạng thái  |");
        System.out.println("+-------+--------------------+--------------------+-------------+");

        for (Object obj : applications) {
            Map<String, Object> app = (Map<String, Object>) obj;
            System.out.printf("| %-5d | %-18s | %-18s | %-11s |\n",
                    app.get("id"),
                    limitLength(app.get("candidateName").toString(), 18),
                    limitLength(app.get("positionName").toString(), 18),
                    app.get("progress"));
        }

        System.out.println("+-------+--------------------+--------------------+-------------+");
    }

    // Format hiển thị chi tiết đơn ứng tuyển
    public void displayApplicationDetail(Map<String, Object> app) {
        if (app.isEmpty()) {
            System.out.println("Không tìm thấy đơn ứng tuyển!");
            return;
        }

        System.out.println("\n=== CHI TIẾT ĐƠN ỨNG TUYỂN ===");
        System.out.println("ID: " + app.get("id"));
        System.out.println("Trạng thái: " + app.get("progress"));

        System.out.println("\n--- THÔNG TIN ỨNG VIÊN ---");
        System.out.println("Tên: " + app.get("candidateName"));
        System.out.println("Email: " + app.get("candidateEmail"));
        System.out.println("Điện thoại: " + app.get("candidatePhone"));
        System.out.println("Kinh nghiệm: " + app.get("candidateExperience") + " năm");
        System.out.println("CV: " + app.get("cvUrl"));

        System.out.println("\n--- THÔNG TIN VỊ TRÍ ---");
        System.out.println("Vị trí: " + app.get("positionName"));
        System.out.println("Mô tả: " + app.get("positionDescription"));
        System.out.println("Lương: " + app.get("minSalary") + " - " + app.get("maxSalary"));
        System.out.println("Yêu cầu kinh nghiệm: " + app.get("minExperience") + " năm");

        System.out.println("\n--- THÔNG TIN PHỎNG VẤN ---");
        Timestamp interviewTime = (Timestamp) app.get("interviewTime");
        System.out.println("Thời gian phỏng vấn: " + (interviewTime != null ? interviewTime : "Chưa xác định"));
        System.out.println("Link phỏng vấn: " +
                (app.get("interviewLink") != null ? app.get("interviewLink") : "Chưa có"));
        System.out.println("Kết quả: " +
                (app.get("interviewResult") != null ? app.get("interviewResult") : "Chưa có"));
        System.out.println("Ghi chú: " +
                (app.get("interviewResultNote") != null ? app.get("interviewResultNote") : "Không có"));
        System.out.println("===================================");
    }

    // Hàm giới hạn độ dài chuỗi (để hiển thị đẹp)
    private String limitLength(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }
}