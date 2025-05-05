package validate.candidate;

import entity.Candidate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ValidateCandidate {
    // Email validation pattern
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    // Phone validation pattern
    private static final String PHONE_PATTERN = "^0[0-9]{9}$";
    // Date format
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    static {
        DATE_FORMAT.setLenient(false);
    }


    // Xác thực đăng ký cơ bản (chỉ name, email, password)
    public static Map<String, String> validateRegistrationBasic(Candidate candidate) {
        Map<String, String> errors = new HashMap<>();

        // Xác thực tên
        if (candidate.getName() == null || candidate.getName().trim().isEmpty()) {
            errors.put("name", "Tên không được để trống");
        } else if (candidate.getName().length() < 3 || candidate.getName().length() > 100) {
            errors.put("name", "Tên phải có từ 3 đến 100 ký tự");
        }

        // Xác thực email
        if (candidate.getEmail() == null || candidate.getEmail().trim().isEmpty()) {
            errors.put("email", "Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, candidate.getEmail())) {
            errors.put("email", "Email không hợp lệ");
        }

        // Xác thực mật khẩu
        if (candidate.getPassword() == null || candidate.getPassword().trim().isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống");
        } else if (candidate.getPassword().length() < 8) {
            errors.put("password", "Mật khẩu phải có ít nhất 8 ký tự");
        }

        return errors;
    }
    // Parse and validate date
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    // Format date to string
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }

    // Validate login credentials (Giữ lại phương thức gốc để tương thích ngược)
    public static Map<String, String> validateLogin(String email, String password) {
        Map<String, String> errors = new HashMap<>();

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, email)) {
            errors.put("email", "Email không hợp lệ");
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống");
        }

        return errors;
    }

    // Nạp chồng phương thức - Chỉ validate email
    public static Map<String, String> validateLoginEmail(String email) {
        Map<String, String> errors = new HashMap<>();

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email không được để trống");
        } else if (!Pattern.matches(EMAIL_PATTERN, email)) {
            errors.put("email", "Email không hợp lệ");
        }

        return errors;
    }

    // Nạp chồng phương thức - Chỉ validate password
    public static Map<String, String> validateLoginPassword(String password) {
        Map<String, String> errors = new HashMap<>();

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống");
        }

        return errors;
    }

    // Prompt and validate string input
    public static String inputString(Scanner scanner, String prompt, String field, Map<String, String> errors) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (errors.containsKey(field)) {
            System.out.println("Lỗi: " + errors.get(field));
        }

        return input;
    }

    // Prompt and validate integer input
    public static int inputInt(Scanner scanner, String prompt, int defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá trị phải là số nguyên. Sử dụng giá trị mặc định: " + defaultValue);
            return defaultValue;
        }
    }

    // Prompt and validate date input
    public static Date inputDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return null;
        }

        Date date = parseDate(input);
        if (date == null) {
            System.out.println("Lỗi: Định dạng ngày không hợp lệ (dd/MM/yyyy)");
        }

        return date;
    }
}