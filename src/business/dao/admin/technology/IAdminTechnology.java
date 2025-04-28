package business.dao.admin.technology;

import business.dao.IGenericDao;
import entity.Technology;

public interface IAdminTechnology extends IGenericDao<Technology, Integer> {
    int add(Technology technology);
    int update(Technology technology);
    int delete(Integer id);
}