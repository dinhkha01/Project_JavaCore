package business.service.candidate;

import business.DAO.candidate.CandidateAuthenDaoImpl;
import business.DAO.candidate.ICandidateAuthen;
import config.ColorPrintUtil;
import entity.Candidate;

import java.io.*;

public class CandidateAuthen {
    private static final String SESSION_FILE = "candidate_session.txt";
    private Candidate currentCandidate;
    private ICandidateAuthen candidateDao;

    public CandidateAuthen() {
        candidateDao = new CandidateAuthenDaoImpl();
        loadSession();
    }

    // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu hay không
    public boolean isEmailExists(String email) {
        return candidateDao.findByEmail(email) != null;
    }

    // Lấy dữ liệu ứng viên theo email
    public Candidate findByEmail(String email) {
        return candidateDao.findByEmail(email);
    }

    // Xác thực mật khẩu cho một email cụ thể (chỉ kiểm tra mật khẩu)
    public boolean validatePassword(String email, String password) {
        Candidate candidate = candidateDao.findByEmail(email);
        if (candidate == null) {
            return false; // Email không tồn tại
        }
        return candidate.getPassword().equals(password);
    }

    // Kiểm tra xem đã có phiên đăng nhập không
    public boolean hasActiveSession() {
        return currentCandidate != null;
    }

    // Lấy thông tin candidate hiện tại
    public Candidate getCurrentCandidate() {
        return currentCandidate;
    }

    // Đăng ký tài khoản mới
    public boolean registerCandidate(Candidate candidate) {
        // Kiểm tra xem email đã tồn tại chưa
        if (candidateDao.findByEmail(candidate.getEmail()) != null) {
            ColorPrintUtil.printError("Email đã được sử dụng. Vui lòng sử dụng email khác!");
            return false;
        }

        try {
            // Thêm candidate mới vào database
            candidateDao.save(candidate);
            ColorPrintUtil.printSuccess("Đăng ký thành công! Vui lòng đăng nhập.");
            return true;
        } catch (Exception e) {
            ColorPrintUtil.printError("Lỗi khi đăng ký: " + e.getMessage());
            return false;
        }
    }

    // Đăng nhập candidate
    public boolean loginCandidate(String email, String password) {
        if (hasActiveSession()) {
            ColorPrintUtil.printWarning("Bạn đã đăng nhập với tài khoản: " + currentCandidate.getEmail());
            return true;
        }

        try {
            Candidate candidate = candidateDao.findByEmailAndPassword(email, password);
            if (candidate != null) {
                // Kiểm tra trạng thái tài khoản
                if ("inactive".equalsIgnoreCase(candidate.getStatus())) {
                    ColorPrintUtil.printError("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên để được hỗ trợ.");
                    return false;
                }

                currentCandidate = candidate;
                saveSession(); // Lưu phiên đăng nhập
                ColorPrintUtil.printSuccess("Đăng nhập thành công!");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            ColorPrintUtil.printError("Lỗi khi đăng nhập: " + e.getMessage());
            return false;
        }
    }

    // Đăng xuất candidate
    public void logoutCandidate() {
        currentCandidate = null;
        clearSession();
        ColorPrintUtil.printLogout("Đăng xuất thành công!");
    }

    // Lưu phiên đăng nhập vào file
    private void saveSession() {
        try (FileWriter fw = new FileWriter(SESSION_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.valueOf(currentCandidate.getId()));
            bw.newLine();
            bw.write(currentCandidate.getEmail());
            bw.newLine();
            bw.write(currentCandidate.getPassword());
        } catch (IOException e) {
            ColorPrintUtil.printError("Lỗi khi lưu phiên đăng nhập: " + e.getMessage());
        }
    }

    // Tải phiên đăng nhập từ file
    private void loadSession() {
        File sessionFile = new File(SESSION_FILE);
        if (!sessionFile.exists()) {
            return; // Không có file session
        }

        try (FileReader fr = new FileReader(SESSION_FILE);
             BufferedReader br = new BufferedReader(fr)) {
            String idStr = br.readLine();
            String email = br.readLine();
            String password = br.readLine();

            // Kiểm tra xem phiên đăng nhập có hợp lệ không
            if (idStr != null && email != null && password != null) {
                int id = Integer.parseInt(idStr);
                // Lấy thông tin candidate từ database
                Candidate candidate = candidateDao.findByEmailAndPassword(email, password);
                if (candidate != null && candidate.getId() == id) {
                    // Kiểm tra trạng thái tài khoản khi tải phiên đăng nhập
                    if ("inactive".equalsIgnoreCase(candidate.getStatus())) {
                        ColorPrintUtil.printWarning("Tài khoản của bạn đã bị khóa. Phiên đăng nhập trước đó không hợp lệ.");
                        clearSession();
                        return;
                    }

                    currentCandidate = candidate;
                    ColorPrintUtil.printInfo("Đã tải phiên đăng nhập của ứng viên: " + email);
                }
            }
        } catch (IOException | NumberFormatException e) {
            ColorPrintUtil.printError("Lỗi khi đọc phiên đăng nhập: " + e.getMessage());
            clearSession();
        }
    }

    // Xóa phiên đăng nhập
    private void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            if (sessionFile.delete()) {
                ColorPrintUtil.printError("Đã xóa phiên đăng nhập.");
            } else {
                ColorPrintUtil.printError("Không thể xóa file phiên đăng nhập.");
            }
        }
    }

    // Cập nhật thông tin candidate
    public boolean updateCandidate(Candidate candidate) {
        try {
            candidateDao.save(candidate);
            // Cập nhật lại currentCandidate nếu là người đang đăng nhập
            if (currentCandidate != null && currentCandidate.getId() == candidate.getId()) {
                currentCandidate = candidate;
                saveSession(); // Cập nhật phiên đăng nhập
            }
            ColorPrintUtil.printSuccess("Cập nhật thông tin thành công!");
            return true;
        } catch (Exception e) {
            ColorPrintUtil.printError("Lỗi khi cập nhật thông tin: " + e.getMessage());
            return false;
        }
    }
}