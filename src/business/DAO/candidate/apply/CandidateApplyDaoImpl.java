package business.DAO.candidate.apply;

import config.ConnectionDB;
import entity.Application;
import entity.RecruitmentPosition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateApplyDaoImpl implements ICandidateApply {
    private Connection conn = null;
    private CallableStatement cstmt = null;
    private ResultSet rs = null;

    @Override
    public List<RecruitmentPosition> findAll() {
        List<RecruitmentPosition> activePositions = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_GetAllActivePositions()}");
            rs = cstmt.executeQuery();

            while (rs.next()) {
                RecruitmentPosition position = mapResultSetToPosition(rs);
                activePositions.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return activePositions;
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
        Application application = (Application) o;
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_CreateApplication(?, ?, ?, ?)}");
            cstmt.setInt(1, application.getCandidateId());
            cstmt.setInt(2, application.getRecruitmentPositionId());
            cstmt.setString(3, application.getCvUrl());
            cstmt.registerOutParameter(4, Types.INTEGER);

            cstmt.executeUpdate();
            int newId = cstmt.getInt(4);
            application.setId(newId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    @Override
    public void deleteById(Object id) {
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_CancelApplication(?, ?)}");
            cstmt.setInt(1, (Integer) id);
            cstmt.setString(2, "Cancelled by candidate");
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public List<Application> getApplicationsByCandidateId(int candidateId) {
        List<Application> applications = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            cstmt = conn.prepareCall("{CALL sp_GetApplicationsByCandidateId(?)}");
            cstmt.setInt(1, candidateId);
            rs = cstmt.executeQuery();

            while (rs.next()) {
                Application application = mapResultSetToApplication(rs);
                applications.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return applications;
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

    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
        Application application = new Application();
        application.setId(rs.getInt("id"));
        application.setCandidateId(rs.getInt("candidateId"));
        application.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
        application.setCvUrl(rs.getString("cvUrl"));
        application.setProgress(rs.getString("progress"));

        // Handle potential null values for timestamps
        Timestamp interviewRequestDate = rs.getTimestamp("interviewRequestDate");
        if (interviewRequestDate != null) {
            application.setInterviewRequestDate(interviewRequestDate);
        }

        application.setInterviewRequestResult(rs.getString("interviewRequestResult"));
        application.setInterviewLink(rs.getString("interviewLink"));

        Timestamp interviewTime = rs.getTimestamp("interviewTime");
        if (interviewTime != null) {
            application.setInterviewTime(interviewTime);
        }

        application.setInterviewResult(rs.getString("interviewResult"));
        application.setInterviewResultNote(rs.getString("interviewResultNote"));

        Timestamp destroyAt = rs.getTimestamp("destroyAt");
        if (destroyAt != null) {
            application.setDestroyAt(destroyAt);
        }

        application.setDestroyReason(rs.getString("destroyReason"));

        Timestamp createdAt = rs.getTimestamp("createAt");
        if (createdAt != null) {
            application.setCreatedAt(createdAt);
        }

        Timestamp updatedAt = rs.getTimestamp("updateAt");
        if (updatedAt != null) {
            application.setUpdatedAt(updatedAt);
        }

        return application;
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
