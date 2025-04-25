package validate;
import java.util.Scanner;

public class ValidateAdmin {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static boolean validateLogin(Scanner sc) {
        String username = validateUsername(sc);
        String password = validatePassword(sc);

        return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
    }

    private static String validateUsername(Scanner sc) {
        ValidateString val = new ValidateString(5, 20);
        while (true) {
            String username = InputMethod.inputString(sc, "Nhập username admin:", "Username không được để trống");
            if (val.isVal(username)) {
                return username;
            }
            System.err.println(val.errorMassage());
        }
    }

    private static String validatePassword(Scanner sc) {
        ValidateString val = new ValidateString(8, 50);
        while (true) {
            String password = InputMethod.inputString(sc, "Nhập password admin:", "Password không được để trống");
            if (val.isVal(password)) {
                return password;
            }
            System.err.println(val.errorMassage());
        }
    }
}