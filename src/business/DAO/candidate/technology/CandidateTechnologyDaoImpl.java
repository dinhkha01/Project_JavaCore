package business.DAO.candidate.technology;

import config.ConnectionDB;
import entity.CandidateTechnology;
import entity.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateTechnologyDaoImpl implements ICandidateTechnologyDao {

    @Override
    public List<Technology> getTechnologiesByCandidate(int candidateId) {
        List<Technology> technologies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionDB.openConnection();
            String sql = "SELECT t.id, t.name FROM technology t " +
                    "JOIN candidate_technology ct ON t.id = ct.technologyId " +
                    "WHERE ct.candidateId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, candidateId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technologies.add(technology);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return technologies;
    }

    @Override
    public boolean addTechnologyToCandidate(int candidateId, int technologyId) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            // First check if the relationship already exists
            if (checkCandidateHasTechnology(candidateId, technologyId)) {
                return false; // Already exists
            }

            String sql = "INSERT INTO candidate_technology (candidateId, technologyId) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, candidateId);
            ps.setInt(2, technologyId);
            int rowsAffected = ps.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public boolean removeTechnologyFromCandidate(int candidateId, int technologyId) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            String sql = "DELETE FROM candidate_technology WHERE candidateId = ? AND technologyId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, candidateId);
            ps.setInt(2, technologyId);
            int rowsAffected = ps.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public boolean checkCandidateHasTechnology(int candidateId, int technologyId) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exists = false;

        try {
            conn = ConnectionDB.openConnection();
            String sql = "SELECT COUNT(*) FROM candidate_technology WHERE candidateId = ? AND technologyId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, candidateId);
            ps.setInt(2, technologyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return exists;
    }
}