package business.DAO.candidate;

import business.DAO.IGenericDao;
import entity.Candidate;

public interface ICandidateAuthen extends IGenericDao<Candidate, Integer> {
    Candidate findByEmail(String email);
    Candidate findByEmailAndPassword(String email, String password);
}