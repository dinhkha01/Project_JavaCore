package business.DAO.admin.candidate;

import config.ConnectionDB;
import entity.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateDaoImpl implements IAdminCandidate {

    @Override
    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_GetAllCandidates()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }

    @Override
    public Candidate findById(Integer id) {
        Candidate candidate = null;
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("SELECT * FROM candidate WHERE id = ?");
            cs.setInt(1, id);
            rs = cs.executeQuery();

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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidate;
    }

    @Override
    public void save(Candidate candidate) {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionDB.openConnection();

            // Check if this is an update or insert
            if (candidate.getId() > 0) {
                // Update existing candidate
                cs = conn.prepareCall("{CALL sp_UpdateCandidate(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                cs.setInt(1, candidate.getId());
                cs.setString(2, candidate.getName());
                cs.setString(3, candidate.getEmail());
                cs.setString(4, candidate.getPhone());
                cs.setInt(5, candidate.getExperience());
                cs.setString(6, candidate.getGender());
                cs.setString(7, candidate.getStatus());
                cs.setString(8, candidate.getDescription());
                cs.setDate(9, new java.sql.Date(candidate.getDob().getTime()));
            } else {
                // Insert new candidate
                cs = conn.prepareCall("{CALL sp_InsertCandidate(?, ?, ?, ?, ?, ?, ?, ?)}");
                cs.setString(1, candidate.getName());
                cs.setString(2, candidate.getEmail());
                cs.setString(3, candidate.getPassword());
                cs.setString(4, candidate.getPhone());
                cs.setInt(5, candidate.getExperience());
                cs.setString(6, candidate.getGender());
                cs.setString(7, candidate.getDescription());
                cs.setDate(8, new java.sql.Date(candidate.getDob().getTime()));
            }

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_DeleteCandidate(?)}");
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean toggleLockStatus(int candidateId) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_ToggleCandidateStatus(?, ?)}");
            cs.setInt(1, candidateId);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();
            int result = cs.getInt(2);
            success = (result == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean resetPassword(int candidateId, String newPassword) {
        Connection conn = null;
        CallableStatement cs = null;
        boolean success = false;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_ResetCandidatePassword(?, ?, ?)}");
            cs.setInt(1, candidateId);
            cs.setString(2, newPassword);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            int result = cs.getInt(3);
            success = (result == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public List<Candidate> searchByName(String name) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_SearchCandidatesByName(?)}");
            cs.setString(1, name);
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }

    public List<Candidate> filterByExperience(int minExp, int maxExp) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_FilterCandidatesByExperience(?, ?)}");
            cs.setInt(1, minExp);
            cs.setInt(2, maxExp);
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }

    public List<Candidate> filterByAge(int minAge, int maxAge) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_FilterCandidatesByAge(?, ?)}");
            cs.setInt(1, minAge);
            cs.setInt(2, maxAge);
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }

    public List<Candidate> filterByGender(String gender) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_FilterCandidatesByGender(?)}");
            cs.setString(1, gender);
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }

    public List<Candidate> filterByTechnology(int technologyId) {
        List<Candidate> candidates = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.openConnection();
            cs = conn.prepareCall("{CALL sp_FilterCandidatesByTechnology(?)}");
            cs.setInt(1, technologyId);
            rs = cs.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
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
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                ConnectionDB.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return candidates;
    }
}