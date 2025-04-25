package entity;

public class Admin {
    private static final int DEFAULT_ADMIN_ID = 1;
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    private int id;
    private String username;
    private String password;


    private Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Tài khoản admin mặc định
    public static Admin getDefaultAdmin() {
        return new Admin(DEFAULT_ADMIN_ID, DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
    }

    // Getter methods
    public int getId() {
        return id;
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
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}