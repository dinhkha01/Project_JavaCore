package business.DAO.admin.technology;

import business.DAO.IGenericDao;
import entity.Technology;

public interface IAdminTechnology extends IGenericDao<Technology, Integer> {
    int add(Technology technology);
    int update(Technology technology);
    int delete(Integer id);
}