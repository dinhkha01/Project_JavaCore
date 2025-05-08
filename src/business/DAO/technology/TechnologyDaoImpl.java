package business.DAO.technology;

import config.ConnectionDB;
import entity.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDaoImpl implements ITechnologyDao {

    @Override
    public List<Technology> findAll() {
        List<Technology> technologyList = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_GetAllTechnologies()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Technology technology = extractTechnologyFromResultSet(rs);
                technologyList.add(technology);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return technologyList;
    }

    @Override
    public Technology findById(int id) {
        Technology technology = null;
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_GetTechnologyById(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                technology = extractTechnologyFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return technology;
    }

    @Override
    public Technology findById(Object id) {
        return findById((Integer) id);
    }

    @Override
    public void save(Object o) {
        // Implementation not needed for current functionality
    }

    @Override
    public void deleteById(Object id) {
        // Implementation not needed for current functionality
    }

    @Override
    public boolean add(String name) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_AddTechnology(?, ?)}");
            cs.setString(1, name);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            int result = cs.getInt(2);
            success = result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public boolean update(int id, String name) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_UpdateTechnology(?, ?, ?)}");
            cs.setInt(1, id);
            cs.setString(2, name);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();
            int result = cs.getInt(3);
            success = result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public int delete(int id) {
        Connection conn = null;
        CallableStatement cs = null;
        int result = 0;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_DeleteTechnology(?, ?)}");
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            result = cs.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return result;
    }

    @Override
    public List<Technology> findByName(String name) {
        List<Technology> technologyList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionDB.openConnection();
            ps = conn.prepareStatement("SELECT * FROM technology WHERE name LIKE ?");
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Technology technology = extractTechnologyFromResultSet(rs);
                technologyList.add(technology);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return technologyList;
    }

    private Technology extractTechnologyFromResultSet(ResultSet rs) throws SQLException {
        Technology technology = new Technology();
        technology.setId(rs.getInt("id"));
        technology.setName(rs.getString("name"));
        return technology;
    }
}