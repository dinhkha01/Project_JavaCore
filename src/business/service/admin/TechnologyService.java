package business.service.admin;

import business.DAO.admin.technology.TechnologyDaoImpl;
import entity.Technology;
import validate.admin.ValidateTechnology;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TechnologyService {
    private final TechnologyDaoImpl technologyDao;
    private final ValidateTechnology validateTechnology;

    public TechnologyService() throws SQLException {
        this.technologyDao = new TechnologyDaoImpl();
        this.validateTechnology = new ValidateTechnology();
    }

    /**
     * Lấy danh sách công nghệ không bị xóa mềm (không kết thúc bằng *deleted)
     * @return Danh sách công nghệ đang hoạt động
     */
    public List<Technology> getAllActiveTechnologies() {
        List<Technology> allTechnologies = technologyDao.findAll();
        // Lọc ra các công nghệ không kết thúc bằng *deleted
        return allTechnologies.stream()
                .filter(tech -> !tech.getName().endsWith("_deleted"))
                .collect(Collectors.toList());
    }

    /**
     * Thêm công nghệ mới
     * @param name Tên công nghệ
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addTechnology(String name) {
        // Kiểm tra tên không trùng với công nghệ đang hoạt động
        if (!validateTechnology.isNameUnique(name)) {
            return false;
        }

        Technology technology = new Technology();
        technology.setName(name);

        int result = technologyDao.add(technology);
        return result >0;
    }

    /**
     * Cập nhật thông tin công nghệ
     * @param id ID công nghệ cần cập nhật
     * @param newName Tên mới của công nghệ
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTechnology(int id, String newName) {
        // Lấy công nghệ theo ID
        Technology existingTech = technologyDao.findById(id);
        if (existingTech == null) {
            return false;
        }

        // Nếu tên không thay đổi, không cần kiểm tra trùng
        if (existingTech.getName().equals(newName)) {
            return true;
        }

        // Kiểm tra tên không trùng, bỏ qua ID hiện tại
        if (!validateTechnology.isNameUnique(newName, id)) {
            return false;
        }

        existingTech.setName(newName);
        int result = technologyDao.update(existingTech);
        return result > 0;
    }

    /**
     * Xóa công nghệ theo ID
     * @param id ID công nghệ cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteTechnology(int id) {
        // Kiểm tra công nghệ tồn tại
        Technology technology = technologyDao.findById(id);
        if (technology == null) {
            return false;
        }

        try {
            // Thử xóa công nghệ trực tiếp
            int result = technologyDao.delete(id);
            return result > 0;
        } catch (Exception e) {
            // Nếu có lỗi (ví dụ ràng buộc khóa ngoại), thực hiện xóa mềm
            return softDeleteTechnology(technology);
        }
    }

    /**
     * Xóa mềm công nghệ bằng cách đổi tên
     * @param technology Đối tượng công nghệ cần xóa mềm
     * @return true nếu xóa mềm thành công, false nếu thất bại
     */
    private boolean softDeleteTechnology(Technology technology) {
        String oldName = technology.getName();
        // Kiểm tra nếu đã bị xóa mềm trước đó
        if (oldName.endsWith("_deleted")) {
            return true;
        }

        // Đổi tên thành [tên cũ + *deleted]
        String newName = oldName + "_deleted";
        technology.setName(newName);

        int result = technologyDao.update(technology);
        return result > 0;
    }

    /**
     * Kiểm tra công nghệ có tồn tại theo ID
     * @param id ID công nghệ cần kiểm tra
     * @return true nếu tồn tại, false nếu không tồn tại
     */
    public boolean existsById(int id) {
        return technologyDao.findById(id) != null;
    }

    /**
     * Lấy công nghệ theo ID
     * @param id ID công nghệ cần lấy
     * @return Đối tượng Technology, null nếu không tìm thấy
     */
    public Technology getTechnologyById(int id) {
        return technologyDao.findById(id);
    }
}