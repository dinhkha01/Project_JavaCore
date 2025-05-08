package business.DAO.technology;

import business.DAO.IGenericDao;
import entity.Technology;

import java.util.List;

public interface ITechnologyDao extends IGenericDao {
    List<Technology> findAll();
    Technology findById(int id);
    boolean add(String name);
    boolean update(int id, String name);
    int delete(int id);
    List<Technology> findByName(String name);
}
