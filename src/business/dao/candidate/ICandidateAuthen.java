package business.dao.candidate;

import business.dao.IGenericDao;
import entity.Candidate;

public interface ICandidateAuthen extends IGenericDao<Candidate, Integer> {
    Candidate findByEmail(String email);
    Candidate findByEmailAndPassword(String email, String password);
}