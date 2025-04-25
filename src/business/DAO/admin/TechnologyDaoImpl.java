package business.DAO.admin;

import config.ConnectionDB;
import entity.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDaoImpl implements IAdminTechnology {
    private final Connection connection;

    public TechnologyDaoImpl() throws SQLException {
        // Khởi tạo kết nối từ DBContext
        this.connection = ConnectionDB.openConnection();
    }

    @Override
    public List<Technology> findAll() {
        List<Technology> technologies = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall("{CALL sp_GetAllTechnologies()}")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Technology technology = new Technology();
                technology.setId(resultSet.getInt("id"));
                technology.setName(resultSet.getString("name"));
                technologies.add(technology);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technologies;
    }

    @Override
    public Technology findById(Integer id) {
        Technology technology = null;

        try (CallableStatement statement = connection.prepareCall("{CALL sp_GetTechnologyById(?)}")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                technology = new Technology();
                technology.setId(resultSet.getInt("id"));
                technology.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technology;
    }

    @Override
    public void save(Technology technology) {
        if (technology.getId() > 0) {
            // Nếu đã có ID, thực hiện cập nhật
            update(technology);
        } else {
            // Chưa có ID, thực hiện thêm mới
            add(technology);
        }
    }

    @Override
    public int add(Technology technology) {
        int result = 0;

        try (CallableStatement statement = connection.prepareCall("{CALL sp_AddTechnology(?, ?)}")) {
            statement.setString(1, technology.getName());
            statement.registerOutParameter(2, Types.INTEGER);

            statement.execute();
            result = statement.getInt(2);

            if (result > 0) {
                // Cập nhật ID cho đối tượng nếu thêm thành công
                technology.setId(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int update(Technology technology) {
        int result = 0;

        try (CallableStatement statement = connection.prepareCall("{CALL sp_UpdateTechnology(?, ?, ?)}")) {
            statement.setInt(1, technology.getId());
            statement.setString(2, technology.getName());
            statement.registerOutParameter(3, Types.INTEGER);

            statement.execute();
            result = statement.getInt(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int delete(Integer id) {
        int result = 0;

        try (CallableStatement statement = connection.prepareCall("{CALL sp_DeleteTechnology(?, ?)}")) {
            statement.setInt(1, id);
            statement.registerOutParameter(2, Types.INTEGER);

            statement.execute();
            result = statement.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void deleteById(Integer id) {
        delete(id);
    }
}