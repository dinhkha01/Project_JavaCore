package business.service.candidate.technology;

import business.DAO.candidate.technology.CandidateTechnologyDaoImpl;
import business.DAO.candidate.technology.ICandidateTechnologyDao;
import entity.Technology;

import java.util.List;

public class CandidateTechnologyServiceImpl implements ICandidateTechnologyService {
    private ICandidateTechnologyDao candidateTechnologyDao;

    public CandidateTechnologyServiceImpl() {
        this.candidateTechnologyDao = new CandidateTechnologyDaoImpl();
    }

    @Override
    public List<Technology> getCandidateTechnologies(int candidateId) {
        return candidateTechnologyDao.getTechnologiesByCandidate(candidateId);
    }

    @Override
    public boolean addTechnologyToCandidate(int candidateId, int technologyId) {
        return candidateTechnologyDao.addTechnologyToCandidate(candidateId, technologyId);
    }

    @Override
    public boolean removeTechnologyFromCandidate(int candidateId, int technologyId) {
        return candidateTechnologyDao.removeTechnologyFromCandidate(candidateId, technologyId);
    }

    @Override
    public boolean hasTechnology(int candidateId, int technologyId) {
        return candidateTechnologyDao.checkCandidateHasTechnology(candidateId, technologyId);
    }
}