package business.service.candidate.infor;

import business.DAO.candidate.infor.CandidateInforDaoImpl;
import business.DAO.candidate.infor.ICandidateInforDao;
import entity.Candidate;

public class CandidateServiceImpl implements ICandidateService {
    private ICandidateInforDao candidateInforDao;

    public CandidateServiceImpl() {
        this.candidateInforDao = new CandidateInforDaoImpl();
    }

    @Override
    public boolean changePassword(String identifier, String oldPassword, String newPassword) {
        if (identifier.contains("@")) {
            return candidateInforDao.changePasswordByEmail(identifier, oldPassword, newPassword);
        } else {
            return candidateInforDao.changePasswordByPhone(identifier, oldPassword, newPassword);
        }
    }

    @Override
    public boolean updateCandidateInfo(Candidate candidate) {
        return candidateInforDao.updateCandidateInfo(candidate);
    }

    @Override
    public Candidate getCandidateByEmail(String email) {
        return candidateInforDao.findByEmail(email);
    }

    @Override
    public Candidate getCandidateByPhone(String phone) {
        return candidateInforDao.findByPhone(phone);
    }

    @Override
    public Candidate getCandidateById(int id) {
        return (Candidate) candidateInforDao.findById(id);
    }

    @Override
    public boolean verifyPassword(String identifier, String password) {
        Candidate candidate;
        if (identifier.contains("@")) {
            candidate = getCandidateByEmail(identifier);
        } else {
            candidate = getCandidateByPhone(identifier);
        }

        if (candidate != null) {
            return candidate.getPassword().equals(password);
        }
        return false;
    }
}