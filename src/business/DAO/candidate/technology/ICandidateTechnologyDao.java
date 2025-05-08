package business.DAO.candidate.technology;

import entity.CandidateTechnology;
import entity.Technology;

import java.util.List;

public interface ICandidateTechnologyDao {
    List<Technology> getTechnologiesByCandidate(int candidateId);
    boolean addTechnologyToCandidate(int candidateId, int technologyId);
    boolean removeTechnologyFromCandidate(int candidateId, int technologyId);
    boolean checkCandidateHasTechnology(int candidateId, int technologyId);
}