package business.DAO.candidate;

import config.ConnectionDB;
import entity.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateAuthenDaoImpl implements ICandidateAuthen {
    public static final String FIND_ALL = "{CALL proc_candidates_findAll()}";
    public static final String FIND_BY_ID = "{CALL proc_candidates_findById(?)}";
    public static final String CREATE = "{CALL proc_candidates_create(?,?,?,?,?,?,?,?,?)}";
    public static final String UPDATE = "{CALL proc_candidates_update(?,?,?,?,?,?,?,?,?,?)}";
    public static final String DELETE = "{CALL proc_candidates_delete(?)}";
    public static final String FIND_BY_EMAIL = "{CALL proc_candidates_findByEmail(?)}";
    public static final String FIND_BY_EMAIL_AND_PASSWORD = "{CALL proc_candidates_findByEmailAndPassword(?,?)}";
    @Override
    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall(FIND_ALL);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
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
                candidates.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
        return candidates;
    }

    @Override
    public Candidate findById(Integer id) {
        Connection conn = null;
        CallableStatement callSt = null;
        Candidate candidate = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall(FIND_BY_ID);
            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                candidate = new Candidate();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
        return candidate;
    }

    @Override
    public void save(Candidate candidate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            if (candidate.getId() == 0) {
                // Insert new candidate
                callSt = conn.prepareCall(CREATE);
                callSt.setString(1, candidate.getName());
                callSt.setString(2, candidate.getEmail());
                callSt.setString(3, candidate.getPassword());
                callSt.setString(4, candidate.getPhone());
                callSt.setInt(5, candidate.getExperience());
                callSt.setString(6, candidate.getGender());
                callSt.setString(7, candidate.getStatus());
                callSt.setString(8, candidate.getDescription());
                callSt.setDate(9, candidate.getDob());
            } else {
                // Update existing candidate
                callSt = conn.prepareCall(UPDATE);
                callSt.setString(1, candidate.getName());
                callSt.setString(2, candidate.getEmail());
                callSt.setString(3, candidate.getPassword());
                callSt.setString(4, candidate.getPhone());
                callSt.setInt(5, candidate.getExperience());
                callSt.setString(6, candidate.getGender());
                callSt.setString(7, candidate.getStatus());
                callSt.setString(8, candidate.getDescription());
                callSt.setDate(9, candidate.getDob());
                callSt.setInt(10, candidate.getId());
            }
            callSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall(DELETE);
            callSt.setInt(1, id);
            callSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
    }

    @Override
    public Candidate findByEmail(String email) {
        Connection conn = null;
        CallableStatement callSt = null;
        Candidate candidate = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall(FIND_BY_EMAIL);
            callSt.setString(1, email);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                candidate = new Candidate();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
        return candidate;
    }

    @Override
    public Candidate findByEmailAndPassword(String email, String password) {
        Connection conn = null;
        CallableStatement callSt = null;
        Candidate candidate = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall(FIND_BY_EMAIL_AND_PASSWORD);
            callSt.setString(1, email);
            callSt.setString(2, password);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                candidate = new Candidate();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn);
        }
        return candidate;
    }
}