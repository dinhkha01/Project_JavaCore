package business.DAO.admin.candidate;

import business.DAO.IGenericDao;
import entity.Candidate;

import java.util.List;

public interface IAdminCandidate extends IGenericDao<Candidate, Integer> {
    boolean toggleLockStatus(int candidateId);
    boolean resetPassword(int candidateId, String newPassword);
    List<Candidate> searchByName(String name);
    List<Candidate> filterByExperience(int minExp, int maxExp);
    List<Candidate> filterByAge(int minAge, int maxAge);
    List<Candidate> filterByGender(String gender);
    List<Candidate> filterByTechnology(int technologyId);
}