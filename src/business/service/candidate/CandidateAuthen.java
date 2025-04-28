package business.service.candidate;

import business.dao.candidate.CandidateAuthenDaoImpl;
import business.dao.candidate.ICandidateAuthen;
import entity.Candidate;
import validate.candidate.ValidateCandidate;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class CandidateAuthen {
    private static final String SESSION_FILE = "candidate_session.txt";
    private Candidate currentCandidate;
    private ICandidateAuthen candidateDao;
    public boolean isEmailExists(String email) {
        return candidateDao.findByEmail(email) != null;
    }

    // Thêm phương thức mới để lấy dữ liệu ứng viên theo email
    public Candidate findByEmail(String email) {
        return candidateDao.findByEmail(email);
    }
    public CandidateAuthen() {
        candidateDao = new CandidateAuthenDaoImpl();
        loadSession();
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
            System.out.println("Email đã được sử dụng. Vui lòng sử dụng email khác!");
            return false;
        }

        try {
            // Thêm candidate mới vào database
            candidateDao.save(candidate);
            System.out.println("Đăng ký thành công! Vui lòng đăng nhập.");
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng ký: " + e.getMessage());
            return false;
        }
    }

    // Đăng nhập candidate
    public boolean loginCandidate(String email, String password) {
        if (hasActiveSession()) {
            System.out.println("Bạn đã đăng nhập với tài khoản: " + currentCandidate.getEmail());
            return true;
        }

        try {
            Candidate candidate = candidateDao.findByEmailAndPassword(email, password);
            if (candidate != null) {
                currentCandidate = candidate;
                saveSession(); // Lưu phiên đăng nhập
                System.out.println("Đăng nhập thành công!");
                return true;
            } else {
                System.err.println("Sai email hoặc mật khẩu!");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng nhập: " + e.getMessage());
            return false;
        }
    }

    // Đăng xuất candidate
    public void logoutCandidate() {
        currentCandidate = null;
        clearSession(); // Xóa phiên đăng nhập
        System.out.println("Đăng xuất thành công!");
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
            System.err.println("Lỗi khi lưu phiên đăng nhập: " + e.getMessage());
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
                    currentCandidate = candidate;
                    System.out.println("Đã tải phiên đăng nhập của ứng viên: " + email);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi đọc phiên đăng nhập: " + e.getMessage());
            clearSession();
        }
    }

    // Xóa phiên đăng nhập
    private void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            if (sessionFile.delete()) {
                System.out.println("Đã xóa phiên đăng nhập.");
            } else {
                System.err.println("Không thể xóa file phiên đăng nhập.");
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
            System.out.println("Cập nhật thông tin thành công!");
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật thông tin: " + e.getMessage());
            return false;
        }
    }
}