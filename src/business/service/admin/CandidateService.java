package business.service.admin;

import business.DAO.admin.candidate.CandidateDaoImpl;
import entity.Candidate;

import java.util.List;
import java.util.Random;

public class CandidateService {
    private CandidateDaoImpl candidateDao;

    public CandidateService() {
        this.candidateDao = new CandidateDaoImpl();
    }

    public List<Candidate> getAllCandidates() {
        return candidateDao.findAll();
    }

    public Candidate getCandidateById(int id) {
        return candidateDao.findById(id);
    }

    public boolean toggleCandidateStatus(int candidateId) {
        return candidateDao.toggleLockStatus(candidateId);
    }

    public String resetCandidatePassword(int candidateId) {
        String newPassword = generateRandomPassword();
        boolean result = candidateDao.resetPassword(candidateId, newPassword);

        if (result) {
            return newPassword;
        } else {
            return null;
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Generate a password with at least 8 characters
        sb.append("Temp@");
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    public List<Candidate> searchCandidatesByName(String name) {
        return candidateDao.searchByName(name);
    }

    public List<Candidate> filterCandidatesByExperience(int minExp, int maxExp) {
        return candidateDao.filterByExperience(minExp, maxExp);
    }

    public List<Candidate> filterCandidatesByAge(int minAge, int maxAge) {
        return candidateDao.filterByAge(minAge, maxAge);
    }

    public List<Candidate> filterCandidatesByGender(String gender) {
        return candidateDao.filterByGender(gender);
    }

    public List<Candidate> filterCandidatesByTechnology(int technologyId) {
        return candidateDao.filterByTechnology(technologyId);
    }
}