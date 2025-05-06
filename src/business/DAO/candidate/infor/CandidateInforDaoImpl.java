package business.DAO.candidate.infor;

import config.ConnectionDB;
import entity.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateInforDaoImpl implements ICandidateInforDao {
    @Override
    public List<Candidate> findAll() {
        List<Candidate> candidateList = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("SELECT * FROM candidate");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = extractCandidateFromResultSet(rs);
                candidateList.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return candidateList;
    }

    @Override
    public Candidate findById(Object id) {
        Connection conn = null;
        CallableStatement cs = null;
        Candidate candidate = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_getCandidateById(?)}");
            cs.setInt(1, (Integer) id);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                candidate = extractCandidateFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return candidate;
    }

    @Override
    public void save(Object o) {
        // Implementation of save method if needed
        // Not required for current functionalities
    }

    @Override
    public void deleteById(Object id) {
        // Implementation of deleteById method if needed
        // Not required for current functionalities
    }

    @Override
    public Candidate findByEmail(String email) {
        Connection conn = null;
        CallableStatement cs = null;
        Candidate candidate = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_getCandidateByEmail(?)}");
            cs.setString(1, email);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                candidate = extractCandidateFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return candidate;
    }

    @Override
    public Candidate findByPhone(String phone) {
        Connection conn = null;
        CallableStatement cs = null;
        Candidate candidate = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_getCandidateByPhone(?)}");
            cs.setString(1, phone);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                candidate = extractCandidateFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return candidate;
    }

    @Override
    public boolean changePasswordByEmail(String email, String oldPassword, String newPassword) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_changePasswordByEmail(?, ?, ?, ?)}");
            cs.setString(1, email);
            cs.setString(2, oldPassword);
            cs.setString(3, newPassword);
            cs.registerOutParameter(4, Types.BOOLEAN);

            cs.execute();
            success = cs.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public boolean changePasswordByPhone(String phone, String oldPassword, String newPassword) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_changePasswordByPhone(?, ?, ?, ?)}");
            cs.setString(1, phone);
            cs.setString(2, oldPassword);
            cs.setString(3, newPassword);
            cs.registerOutParameter(4, Types.BOOLEAN);

            cs.execute();
            success = cs.getBoolean(4);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    @Override
    public boolean updateCandidateInfo(Candidate candidate) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_updateCandidateInfo(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, candidate.getId());
            cs.setString(2, candidate.getName());
            cs.setString(3, candidate.getPhone());
            cs.setInt(4, candidate.getExperience());
            cs.setString(5, candidate.getGender());
            cs.setString(6, candidate.getDescription());
            cs.setDate(7, candidate.getDob());

            int rowsAffected = cs.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }

        return success;
    }

    private Candidate extractCandidateFromResultSet(ResultSet rs) throws SQLException {
        Candidate candidate = new Candidate();
        candidate.setId(rs.getInt("id"));
        candidate.setName(rs.getString("name"));
        candidate.setEmail(rs.getString("email"));
        candidate.setPassword(rs.getString("password"));
        candidate.setPhone(rs.getString("phone"));
        candidate.setExperience(rs.getInt("experience"));
        candidate.setGender(rs.getString("gender"));
        candidate.setStatus(rs.getString("status"));
        candidate.setDescription(rs.getString("description"));
        candidate.setDob(rs.getDate("dob"));
        return candidate;
    }
}