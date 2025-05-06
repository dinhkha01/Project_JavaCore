
package business.DAO.candidate.infor;

import business.DAO.IGenericDao;
import entity.Candidate;

public interface ICandidateInforDao extends IGenericDao {
    Candidate findByEmail(String email);
    Candidate findByPhone(String phone);
    boolean changePasswordByEmail(String email, String oldPassword, String newPassword);
    boolean changePasswordByPhone(String phone, String oldPassword, String newPassword);
    boolean updateCandidateInfo(Candidate candidate);
}