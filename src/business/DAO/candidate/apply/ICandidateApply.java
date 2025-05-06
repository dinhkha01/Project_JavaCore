package business.DAO.candidate.apply;

import business.DAO.IGenericDao;
import entity.Application;
import entity.RecruitmentPosition;

import java.util.List;

public interface ICandidateApply extends IGenericDao{
    List<Application> getApplicationsByCandidateId(int candidateId);
}