package business.service.candidate.infor;

import entity.Candidate;

public interface ICandidateService {
    boolean changePassword(String identifier, String oldPassword, String newPassword);
    boolean updateCandidateInfo(Candidate candidate);
    Candidate getCandidateByEmail(String email);
    Candidate getCandidateByPhone(String phone);
    Candidate getCandidateById(int id);
    boolean verifyPassword(String identifier, String password);
}