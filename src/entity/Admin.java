package entity;

public class Admin {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    private String username;
    private String password;

    private Admin( String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Tài khoản admin mặc định
    public static Admin getDefaultAdmin() {
        return new Admin( DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                ", username='" + username + '\'' +
                '}';
    }
}