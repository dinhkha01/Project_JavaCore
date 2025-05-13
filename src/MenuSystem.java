import business.service.admin.AdminManagerAuthen;
import business.service.candidate.CandidateAuthen;
import config.ColorPrintUtil;
import entity.Candidate;
import presentation.admin.MenuAdmin;
import presentation.candidate.MenuCandidate;
import validate.candidate.ValidateCandidateAuthen;

import java.util.*;

public class MenuSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminManagerAuthen adminManager = new AdminManagerAuthen();
        CandidateAuthen candidateAuthen = new CandidateAuthen();

        // Kiểm tra phiên đăng nhập trước khi hiển thị menu chính
        if (adminManager.hasActiveSession()) {
            // Nếu đã có phiên đăng nhập admin, chuyển thẳng đến menu admin
            MenuAdmin adminMenu = new MenuAdmin();
            adminMenu.showMainMenu();
            adminMenu.showMainMenu();
        } else if (candidateAuthen.hasActiveSession()) {
            // Nếu đã có phiên đăng nhập candidate, chuyển thẳng đến menu candidate
            MenuCandidate candidateMenu = new MenuCandidate(candidateAuthen);
            candidateMenu.showMainMenu();
        }

        while (true) {
            ColorPrintUtil.printHeader("HỆ THỐNG TUYỂN DỤNG");
            ColorPrintUtil.printInfo("1. Đăng nhập Admin");
            ColorPrintUtil.printInfo("2. Đăng nhập Ứng viên");
            ColorPrintUtil.printInfo("3. Đăng ký tài khoản Ứng viên");
            ColorPrintUtil.printInfo("4. Thoát");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                continue;
            }

            switch (choice) {
                case 1:
                    if (adminManager.loginAdmin()) {
                        MenuAdmin adminMenu = new MenuAdmin();
                        adminMenu.showMainMenu();
                    }
                    break;
                case 2:
                    loginCandidate(candidateAuthen);
                    break;
                case 3:
                    registerCandidate();
                    break;
                case 4:
                    ColorPrintUtil.printSuccess("Đã thoát chương trình!");
                    System.exit(0);
                default:
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void loginCandidate(   CandidateAuthen candidateAuthen) {
        Scanner scanner = new Scanner(System.in);
        ColorPrintUtil.printSubHeader("ĐĂNG NHẬP ỨNG VIÊN");

        // Input và validate email
        String email = "";
        boolean isEmailValid = false;
        Candidate existingCandidate = null;

        while (!isEmailValid) {
            ColorPrintUtil.printPrompt("Nhập email: ");
            email = scanner.nextLine().trim();

            // Sử dụng phương thức validateLoginEmail để kiểm tra định dạng email
            Map<String, String> emailErrors = ValidateCandidateAuthen.validateLoginEmail(email);

            if (!emailErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + emailErrors.get("email"));
            } else {
                // Kiểm tra xem email có tồn tại trong hệ thống không
                existingCandidate = candidateAuthen.findByEmail(email);
                if (existingCandidate == null) {
                    ColorPrintUtil.printError("Lỗi: Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại hoặc đăng ký tài khoản mới.");
                } else {
                    if ("inactive".equalsIgnoreCase(existingCandidate.getStatus())) {
                        ColorPrintUtil.printError("Lỗi: Tài khoản này đã bị khóa. Vui lòng liên hệ quản trị viên để được hỗ trợ.");
                        return;
                    }
                    isEmailValid = true;
                }
            }
        }

        // Input và validate password
        String password = "";
        boolean isPasswordValid = false;
        while (!isPasswordValid) {
            ColorPrintUtil.printPrompt("Nhập mật khẩu: ");
            password = scanner.nextLine().trim();

            // Sử dụng phương thức validateLoginPassword để kiểm tra định dạng password
            Map<String, String> passwordErrors = ValidateCandidateAuthen.validateLoginPassword(password);

            if (!passwordErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + passwordErrors.get("password"));
            } else {
                isPasswordValid = true;
            }
        }

        // Đăng nhập sau khi đã validate email và password
        if (candidateAuthen.loginCandidate(email, password)) {
            MenuCandidate candidateMenu = new MenuCandidate(candidateAuthen);
            candidateMenu.showMainMenu();
        } else {
            // Nếu đăng nhập thất bại (mật khẩu không đúng, vì email đã được xác nhận tồn tại và không bị khóa)
            ColorPrintUtil.printError("Mật khẩu không đúng! Vui lòng thử lại.");
            // Cho phép người dùng nhập lại mật khẩu mà không cần nhập lại email
            retryPasswordLogin(candidateAuthen, email);
        }
    }

    // Phương thức mới để nhập lại mật khẩu mà không cần nhập lại email
    private static void retryPasswordLogin(CandidateAuthen candidateAuthen, String email) {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;

        while (attempts < MAX_ATTEMPTS) {
            ColorPrintUtil.printPrompt("Nhập mật khẩu: ");
            String password = scanner.nextLine().trim();

            // Xác thực định dạng password
            Map<String, String> passwordErrors = ValidateCandidateAuthen.validateLoginPassword(password);

            if (!passwordErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + passwordErrors.get("password"));
                continue;
            }

            // Thử đăng nhập
            if (candidateAuthen.loginCandidate(email, password)) {
                MenuCandidate candidateMenu = new MenuCandidate(candidateAuthen);
                candidateMenu.showMainMenu();
                return;
            } else {
                attempts++;
                if (attempts < MAX_ATTEMPTS) {
                    ColorPrintUtil.printWarning("Mật khẩu không đúng! Còn " + (MAX_ATTEMPTS - attempts) + " lần thử.");
                }
            }
        }

        ColorPrintUtil.printError("Đã vượt quá số lần thử. Vui lòng thử lại sau.");
    }

    private static void registerCandidate() {
        Scanner scanner = new Scanner(System.in);
        Candidate candidate = new Candidate();
        CandidateAuthen candidateAuthen = new CandidateAuthen();

        ColorPrintUtil.printSubHeader("ĐĂNG KÝ TÀI KHOẢN ỨNG VIÊN");

        // Nhập và xác thực họ tên
        boolean isNameValid = false;
        while (!isNameValid) {
            ColorPrintUtil.printPrompt("Nhập họ tên: ");
            String name = scanner.nextLine().trim();
            candidate.setName(name);

            Map<String, String> nameErrors = new HashMap<>();
            if (name == null || name.trim().isEmpty()) {
                nameErrors.put("name", "Tên không được để trống");
            } else if (name.length() < 3 || name.length() > 100) {
                nameErrors.put("name", "Tên phải có từ 3 đến 100 ký tự");
            }

            if (!nameErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + nameErrors.get("name"));
            } else {
                isNameValid = true;
            }
        }

        // Nhập và xác thực email
        boolean isEmailValid = false;
        while (!isEmailValid) {
            ColorPrintUtil.printPrompt("Nhập email: ");
            String email = scanner.nextLine().trim();
            candidate.setEmail(email);

            Map<String, String> emailErrors = ValidateCandidateAuthen.validateLoginEmail(email);

            if (!emailErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + emailErrors.get("email"));
            } else if (candidateAuthen.isEmailExists(email)) {
                // Email đã tồn tại trong hệ thống
                ColorPrintUtil.printError("Lỗi: Email này đã được đăng ký. Vui lòng sử dụng email khác");
            } else {
                isEmailValid = true;
            }
        }

        // Nhập và xác thực mật khẩu
        boolean isPasswordValid = false;
        while (!isPasswordValid) {
            ColorPrintUtil.printPrompt("Nhập mật khẩu: ");
            String password = scanner.nextLine().trim();
            candidate.setPassword(password);

            Map<String, String> passwordErrors = ValidateCandidateAuthen.validateLoginPassword(password);

            if (!passwordErrors.isEmpty()) {
                ColorPrintUtil.printError("Lỗi: " + passwordErrors.get("password"));
            } else if (password.length() < 8) {
                ColorPrintUtil.printError("Lỗi: Mật khẩu phải có ít nhất 8 ký tự");
            } else {
                isPasswordValid = true;
            }
        }

        // Đặt các giá trị mặc định cho các trường không bắt buộc
        candidate.setPhone("");
        candidate.setExperience(0);
        candidate.setGender("");
        candidate.setDescription("");
        candidate.setStatus("active");

        // Đăng ký ứng viên (đã được xác thực từng trường)
        if (candidateAuthen.registerCandidate(candidate)) {
            ColorPrintUtil.printSuccess("Đăng ký thành công! Bạn có thể đăng nhập và cập nhật thông tin chi tiết sau.");
        }
    }
}