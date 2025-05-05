package business.DAO.admin.applicationForm;

import config.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ApplicationDaoImpl implements IApplication {
    @Override
    public List<Object> findAll() {
        List<Map<String, Object>> applications = new ArrayList<>();
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_GetAllActiveApplications()}");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> app = new HashMap<>();
                app.put("id", resultSet.getInt("id"));
                app.put("candidateId", resultSet.getInt("candidateId"));
                app.put("recruitmentPositionId", resultSet.getInt("recruitmentPositionId"));
                app.put("cvUrl", resultSet.getString("cvUrl"));
                app.put("progress", resultSet.getString("progress"));
                app.put("candidateName", resultSet.getString("candidateName"));
                app.put("positionName", resultSet.getString("positionName"));
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(applications);
    }

    @Override
    public Object findById(Object id) {
        Map<String, Object> appDetail = new HashMap<>();
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_ViewApplicationDetail(?)}");
            statement.setInt(1, (Integer) id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Thông tin ứng dụng
                appDetail.put("id", resultSet.getInt("id"));
                appDetail.put("candidateId", resultSet.getInt("candidateId"));
                appDetail.put("recruitmentPositionId", resultSet.getInt("recruitmentPositionId"));
                appDetail.put("cvUrl", resultSet.getString("cvUrl"));
                appDetail.put("progress", resultSet.getString("progress"));
                appDetail.put("interviewRequestDate", resultSet.getTimestamp("interviewRequestDate"));
                appDetail.put("interviewLink", resultSet.getString("interviewLink"));
                appDetail.put("interviewTime", resultSet.getTimestamp("interviewTime"));
                appDetail.put("interviewResult", resultSet.getString("interviewResult"));
                appDetail.put("interviewResultNote", resultSet.getString("interviewResultNote"));

                // Thông tin ứng viên
                appDetail.put("candidateName", resultSet.getString("candidateName"));
                appDetail.put("candidateEmail", resultSet.getString("candidateEmail"));
                appDetail.put("candidatePhone", resultSet.getString("candidatePhone"));
                appDetail.put("candidateExperience", resultSet.getInt("candidateExperience"));

                // Thông tin vị trí
                appDetail.put("positionName", resultSet.getString("positionName"));
                appDetail.put("positionDescription", resultSet.getString("positionDescription"));
                appDetail.put("minSalary", resultSet.getBigDecimal("minSalary"));
                appDetail.put("maxSalary", resultSet.getBigDecimal("maxSalary"));
                appDetail.put("minExperience", resultSet.getInt("minExperience"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return appDetail;
    }

    @Override
    public void save(Object o) {
        // Not used in this implementation
    }

    @Override
    public void deleteById(Object id) {
        // Not used - we use sp_CancelApplication instead
    }

    // Lọc theo progress
    public List<Map<String, Object>> filterByProgress(String progress) {
        List<Map<String, Object>> applications = new ArrayList<>();
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_FilterApplicationsByProgress(?)}");
            statement.setString(1, progress);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> app = new HashMap<>();
                app.put("id", resultSet.getInt("id"));
                app.put("candidateId", resultSet.getInt("candidateId"));
                app.put("recruitmentPositionId", resultSet.getInt("recruitmentPositionId"));
                app.put("cvUrl", resultSet.getString("cvUrl"));
                app.put("progress", resultSet.getString("progress"));
                app.put("candidateName", resultSet.getString("candidateName"));
                app.put("positionName", resultSet.getString("positionName"));
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return applications;
    }

    // Lọc theo kết quả phỏng vấn
    public List<Map<String, Object>> filterByResult(String result) {
        List<Map<String, Object>> applications = new ArrayList<>();
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_FilterApplicationsByResult(?)}");
            statement.setString(1, result);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> app = new HashMap<>();
                app.put("id", resultSet.getInt("id"));
                app.put("candidateId", resultSet.getInt("candidateId"));
                app.put("recruitmentPositionId", resultSet.getInt("recruitmentPositionId"));
                app.put("cvUrl", resultSet.getString("cvUrl"));
                app.put("progress", resultSet.getString("progress"));
                app.put("interviewResult", resultSet.getString("interviewResult"));
                app.put("candidateName", resultSet.getString("candidateName"));
                app.put("positionName", resultSet.getString("positionName"));
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return applications;
    }

    // Hủy đơn ứng tuyển
    public boolean cancelApplication(int appId, String reason) {
        Connection connection = null;
        CallableStatement statement = null;
        boolean success = false;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_CancelApplication(?, ?)}");
            statement.setInt(1, appId);
            statement.setString(2, reason);
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Gửi thông tin phỏng vấn
    public boolean sendInterviewInfo(int appId, String interviewLink, Timestamp interviewTime) {
        Connection connection = null;
        CallableStatement statement = null;
        boolean success = false;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_SendInterviewInfo(?, ?, ?)}");
            statement.setInt(1, appId);
            statement.setString(2, interviewLink);
            statement.setTimestamp(3, interviewTime);
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Cập nhật kết quả phỏng vấn
    public boolean updateInterviewResult(int appId, String result, String note) {
        Connection connection = null;
        CallableStatement statement = null;
        boolean success = false;

        try {
            connection = ConnectionDB.openConnection();
            statement = connection.prepareCall("{CALL sp_UpdateInterviewResult(?, ?, ?)}");
            statement.setInt(1, appId);
            statement.setString(2, result);
            statement.setString(3, note);
            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                ConnectionDB.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}