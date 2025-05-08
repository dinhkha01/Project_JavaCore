package business.service.technology;

import entity.Technology;

import java.util.List;

public interface ITechnologyService {
    List<Technology> getAllTechnologies();
    Technology getTechnologyById(int id);
    boolean addTechnology(String name);
    boolean updateTechnology(int id, String name);
    int deleteTechnology(int id);
    List<Technology> searchTechnologyByName(String name);
}