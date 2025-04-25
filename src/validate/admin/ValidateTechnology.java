package validate.admin;

import business.DAO.admin.TechnologyDaoImpl;
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
     * @return true nếu tên duy nhất, false nếu đã tồn tại
     */
    public boolean isNameUnique(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        List<Technology> technologies = technologyDao.findAll();

        // Kiểm tra xem tên công nghệ đã tồn tại chưa (không phân biệt chữ hoa/thường)
        for (Technology tech : technologies) {
            if (tech.getName().equalsIgnoreCase(name)) {
                return false;
            }

            // Kiểm tra xem có phải là phiên bản đã bị xóa mềm không
            String nameWithoutDeleted = name.endsWith("*deleted") ?
                    name.substring(0, name.length() - 8) : name;

            String techNameWithoutDeleted = tech.getName().endsWith("*deleted") ?
                    tech.getName().substring(0, tech.getName().length() - 8) : tech.getName();

            if (nameWithoutDeleted.equalsIgnoreCase(techNameWithoutDeleted)) {
                return false;
            }
        }

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

            if (tech.getName().equalsIgnoreCase(name)) {
                return false;
            }

            // Kiểm tra xem có phải là phiên bản đã bị xóa mềm không
            String nameWithoutDeleted = name.endsWith("*deleted") ?
                    name.substring(0, name.length() - 8) : name;

            String techNameWithoutDeleted = tech.getName().endsWith("*deleted") ?
                    tech.getName().substring(0, tech.getName().length() - 8) : tech.getName();

            if (nameWithoutDeleted.equalsIgnoreCase(techNameWithoutDeleted)) {
                return false;
            }
        }

        return true;
    }
}