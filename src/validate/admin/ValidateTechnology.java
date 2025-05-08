package validate.admin;

import business.DAO.admin.technology.TechnologyDaoImpl;
import entity.Technology;

import java.sql.SQLException;
import java.util.List;

public class ValidateTechnology {
    private TechnologyDaoImpl technologyDao;

    public ValidateTechnology() {
        try {
            this.technologyDao = new TechnologyDaoImpl();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra tên công nghệ có duy nhất không
     * @param name Tên công nghệ cần kiểm tra
     * @return true nếu tên duy nhất, false nếu đã tồn tại (chỉ kiểm tra công nghệ chưa xóa)
     */
    public boolean isNameUnique(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        List<Technology> technologies = technologyDao.findAll();

        for (Technology tech : technologies) {

            // Bỏ qua các công nghệ đã bị xóa mềm
            if (tech.getName().endsWith("_deleted")) {
                continue;
            }
            // So sánh tên không phân biệt chữ hoa/thường
            if (tech.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        System.out.println("-> Tên hợp lệ");
        return true;
    }

    /**
     * Kiểm tra tên công nghệ có duy nhất không (loại trừ ID hiện tại khi cập nhật)
     * @param name Tên công nghệ cần kiểm tra
     * @param id ID công nghệ đang cập nhật
     * @return true nếu tên duy nhất, false nếu đã tồn tại
     */
    public boolean isNameUnique(String name, int id) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        List<Technology> technologies = technologyDao.findAll();

        for (Technology tech : technologies) {
            // Bỏ qua công nghệ đang cập nhật
            if (tech.getId() == id) {
                continue;
            }
            // Bỏ qua các công nghệ đã bị xóa mềm
            if (tech.getName().endsWith("_deleted")) {
                continue;
            }

            // So sánh tên không phân biệt chữ hoa/thường
            if (tech.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        System.out.println("-> Tên hợp lệ");
        return true;
    }
}