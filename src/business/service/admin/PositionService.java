package business.service.admin;

import business.DAO.admin.position.PositionDaoImpl;
import entity.RecruitmentPosition;
import entity.Technology;
import validate.admin.ValidatePosition;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class PositionService {
    private PositionDaoImpl positionDao;
    private ValidatePosition validatePosition;

    public PositionService() {
        this.positionDao = new PositionDaoImpl();
        this.validatePosition = new ValidatePosition();
    }

    // Lấy tất cả vị trí đang hoạt động
    public List<RecruitmentPosition> getAllActivePositions() {
        return positionDao.findAll();
    }

    // Lấy thông tin chi tiết vị trí
    public RecruitmentPosition getPositionById(int positionId) {
        return positionDao.findById(positionId);
    }

    // Thêm vị trí mới
    public boolean addPosition(RecruitmentPosition position) {
        try {
            positionDao.save(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật vị trí
    public boolean updatePosition(RecruitmentPosition position) {
        try {
            positionDao.save(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa vị trí
    public boolean deletePosition(int positionId) {
        try {
            positionDao.deleteById(positionId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách công nghệ
    public List<Technology> getAllTechnologies() {
        return positionDao.getAllTechnologies();
    }

    // Lấy công nghệ của một vị trí
    public List<Technology> getPositionTechnologies(int positionId) {
        return positionDao.getPositionTechnologies(positionId);
    }

    // Thêm công nghệ cho vị trí
    public void addPositionTechnology(int positionId, int technologyId) {
        positionDao.addPositionTechnology(positionId, technologyId);
    }

    // Xóa tất cả công nghệ của vị trí
    public void clearPositionTechnologies(int positionId) {
        positionDao.clearPositionTechnologies(positionId);
    }
}