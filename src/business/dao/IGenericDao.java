package business.dao;

import java.util.List;

public interface IGenericDao<T,E>{
    List<T> findAll();
    T findById(E id);
    void save(T t);
    void deleteById(E id);
}