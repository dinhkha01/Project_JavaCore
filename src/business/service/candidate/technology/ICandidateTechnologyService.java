package business.service.candidate.technology;

import entity.Technology;

import java.util.List;

public interface ICandidateTechnologyService {
    List<Technology> getCandidateTechnologies(int candidateId);
    boolean addTechnologyToCandidate(int candidateId, int technologyId);
    boolean removeTechnologyFromCandidate(int candidateId, int technologyId);
    boolean hasTechnology(int candidateId, int technologyId);
}