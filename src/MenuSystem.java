import business.service.admin.AdminManagerAuthen;
import business.service.candidate.CandidateAuthen;
import entity.Candidate;
import presentation.admin.MenuAdmin;
import presentation.candidate.MenuCandidate;
import validate.candidate.ValidateCandidate;

import java.util.*;
import java.util.regex.Pattern;

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
        } else if (candidateAuthen.hasActiveSession()) {
            // Nếu đã có phiên đăng nhập candidate, chuyển thẳng đến menu candidate
            MenuCandidate candidateMenu = new MenuCandidate(candidateAuthen);
            candidateMenu.showMainMenu();
        }

        while (true) {
            System.out.println("\n==================== HỆ THỐNG TUYỂN DỤNG ====================");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Đăng nhập Ứng viên");
            System.out.println("3. Đăng ký tài khoản Ứng viên");
            System.out.println("4. Thoát");
            System.out.println("============================================================");
            System.out.print("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ!");
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
                    System.out.println("Đã thoát chương trình!");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void loginCandidate(CandidateAuthen candidateAuthen) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> errors = new HashMap<>();

        System.out.println("\n===== ĐĂNG NHẬP ỨNG VIÊN =====");

        // Input email
        System.out.print("Nhập email: ");
        String email = scanner.nextLine().trim();

        // Input password
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine().trim();

        // Validate login credentials
        errors = ValidateCandidate.validateLogin(email, password);

        if (!errors.isEmpty()) {
            System.out.println("\nĐăng nhập không thành công. Vui lòng sửa các lỗi sau:");
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                System.out.println("- " + entry.getValue());
            }
            return;
        }

        if (candidateAuthen.loginCandidate(email, password)) {
            MenuCandidate candidateMenu = new MenuCandidate(candidateAuthen);
            candidateMenu.showMainMenu();
        }
    }

    private static void registerCandidate() {
        Scanner scanner = new Scanner(System.in);
        Candidate candidate = new Candidate();
        CandidateAuthen candidateAuthen = new CandidateAuthen();

        System.out.println("\n===== ĐĂNG KÝ TÀI KHOẢN ỨNG VIÊN =====");

        // Nhập và xác thực họ tên
        boolean isNameValid = false;
        while (!isNameValid) {
            System.out.print("Nhập họ tên: ");
            String name = scanner.nextLine().trim();
            candidate.setName(name);

            if (name == null || name.trim().isEmpty()) {
                System.out.println("Lỗi: Tên không được để trống");
            } else if (name.length() < 3 || name.length() > 100) {
                System.out.println("Lỗi: Tên phải có từ 3 đến 100 ký tự");
            } else {
                isNameValid = true;
            }
        }

        // Nhập và xác thực email
        boolean isEmailValid = false;
        while (!isEmailValid) {
            System.out.print("Nhập email: ");
            String email = scanner.nextLine().trim();
            candidate.setEmail(email);

            if (email == null || email.trim().isEmpty()) {
                System.out.println("Lỗi: Email không được để trống");
            } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Lỗi: Email không hợp lệ");
            } else if (candidateAuthen.isEmailExists(email)) {
                // Email đã tồn tại trong hệ thống
                System.out.println("Lỗi: Email này đã được đăng ký. Vui lòng sử dụng email khác");
            } else {
                isEmailValid = true;
            }
        }

        // Nhập và xác thực mật khẩu
        boolean isPasswordValid = false;
        while (!isPasswordValid) {
            System.out.print("Nhập mật khẩu: ");
            String password = scanner.nextLine().trim();
            candidate.setPassword(password);

            if (password == null || password.trim().isEmpty()) {
                System.out.println("Lỗi: Mật khẩu không được để trống");
            } else if (password.length() < 8) {
                System.out.println("Lỗi: Mật khẩu phải có ít nhất 8 ký tự");
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
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập và cập nhật thông tin chi tiết sau.");
        }
    }
}