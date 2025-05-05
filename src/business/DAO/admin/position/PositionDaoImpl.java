package business.DAO.admin.position;

import config.ConnectionDB;
import entity.RecruitmentPosition;
import entity.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDaoImpl implements IAdminPosition {
    private Connection conn = null;
    private CallableStatement cstmt = null;
    private ResultSet rs = null;

    @Override
    public List<RecruitmentPosition> findAll() {
        List<RecruitmentPosition> positions = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_getAllPositions()}");
            rs = cstmt.executeQuery();

            while (rs.next()) {
                RecruitmentPosition position = mapResultSetToPosition(rs);
                positions.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return positions;
    }

    @Override
    public RecruitmentPosition findById(Object id) {
        RecruitmentPosition position = null;
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_getPositionById(?)}");
            cstmt.setInt(1, (Integer) id);
            rs = cstmt.executeQuery();

            if (rs.next()) {
                position = mapResultSetToPosition(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return position;
    }

    @Override
    public void save(Object o) {
        RecruitmentPosition position = (RecruitmentPosition) o;
        try {
            conn = ConnectionDB.openConnection();
            if (position.getId() > 0) {
                // Update existing position
                updatePosition(position);
            } else {
                // Create new position
                insertPosition(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void insertPosition(RecruitmentPosition position) throws SQLException {
        cstmt = conn.prepareCall("{CALL sp_addPosition(?, ?, ?, ?, ?, ?, ?)}");
        cstmt.setString(1, position.getName());
        cstmt.setString(2, position.getDescription());
        cstmt.setBigDecimal(3, position.getMinSalary());
        cstmt.setBigDecimal(4, position.getMaxSalary());
        cstmt.setInt(5, position.getMinExperience());
        cstmt.setDate(6, position.getExpiredDate());
        cstmt.registerOutParameter(7, Types.INTEGER);

        cstmt.executeUpdate();
        int newId = cstmt.getInt(7);
        position.setId(newId);
    }

    private void updatePosition(RecruitmentPosition position) throws SQLException {
        cstmt = conn.prepareCall("{CALL sp_updatePosition(?, ?, ?, ?, ?, ?, ?)}");
        cstmt.setInt(1, position.getId());
        cstmt.setString(2, position.getName());
        cstmt.setString(3, position.getDescription());
        cstmt.setBigDecimal(4, position.getMinSalary());
        cstmt.setBigDecimal(5, position.getMaxSalary());
        cstmt.setInt(6, position.getMinExperience());
        cstmt.setDate(7, position.getExpiredDate());

        cstmt.executeUpdate();
    }

    @Override
    public void deleteById(Object id) {
        try {
            conn = ConnectionDB.openConnection();

            // Check if position has dependencies
            boolean hasDependencies = checkPositionDependencies((Integer) id);

            if (hasDependencies) {
                // Mark as deleted
                cstmt = conn.prepareCall("{CALL sp_markPositionAsDeleted(?)}");
                cstmt.setInt(1, (Integer) id);
                cstmt.executeUpdate();
            } else {
                // Physically delete
                cstmt = conn.prepareCall("{CALL sp_deletePosition(?)}");
                cstmt.setInt(1, (Integer) id);
                cstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private boolean checkPositionDependencies(int positionId) throws SQLException {
        cstmt = conn.prepareCall("{CALL sp_checkPositionDependencies(?, ?)}");
        cstmt.setInt(1, positionId);
        cstmt.registerOutParameter(2, Types.BOOLEAN);
        cstmt.execute();
        return cstmt.getBoolean(2);
    }

    public void addPositionTechnology(int positionId, int technologyId) {
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_addPositionTechnologies(?, ?)}");
            cstmt.setInt(1, positionId);
            cstmt.setInt(2, technologyId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void clearPositionTechnologies(int positionId) {
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_clearPositionTechnologies(?)}");
            cstmt.setInt(1, positionId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public List<Technology> getAllTechnologies() {
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_getAllTechnologies()}");
            rs = cstmt.executeQuery();

            while (rs.next()) {
                Technology tech = new Technology();
                tech.setId(rs.getInt("id"));
                tech.setName(rs.getString("name"));
                technologies.add(tech);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return technologies;
    }

    public List<Technology> getPositionTechnologies(int positionId) {
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_getPositionTechnologies(?)}");
            cstmt.setInt(1, positionId);
            rs = cstmt.executeQuery();

            while (rs.next()) {
                Technology tech = new Technology();
                tech.setId(rs.getInt("id"));
                tech.setName(rs.getString("name"));
                technologies.add(tech);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return technologies;
    }

    private RecruitmentPosition mapResultSetToPosition(ResultSet rs) throws SQLException {
        RecruitmentPosition position = new RecruitmentPosition();
        position.setId(rs.getInt("id"));
        position.setName(rs.getString("name"));
        position.setDescription(rs.getString("description"));
        position.setMinSalary(rs.getBigDecimal("minSalary"));
        position.setMaxSalary(rs.getBigDecimal("maxSalary"));
        position.setMinExperience(rs.getInt("minExperience"));
        position.setCreatedDate(rs.getDate("createdDate"));
        position.setExpiredDate(rs.getDate("expiredDate"));
        return position;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (cstmt != null) cstmt.close();
            if (conn != null) ConnectionDB.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}