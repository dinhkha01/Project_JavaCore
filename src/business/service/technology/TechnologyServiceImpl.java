package business.service.technology;

import business.DAO.technology.ITechnologyDao;
import business.DAO.technology.TechnologyDaoImpl;
import entity.Technology;

import java.util.List;

public class TechnologyServiceImpl implements ITechnologyService {
    private ITechnologyDao technologyDao;

    public TechnologyServiceImpl() {
        this.technologyDao = new TechnologyDaoImpl();
    }

    @Override
    public List<Technology> getAllTechnologies() {
        return technologyDao.findAll();
    }

    @Override
    public Technology getTechnologyById(int id) {
        return technologyDao.findById(id);
    }

    @Override
    public boolean addTechnology(String name) {
        return technologyDao.add(name);
    }

    @Override
    public boolean updateTechnology(int id, String name) {
        return technologyDao.update(id, name);
    }

    @Override
    public int deleteTechnology(int id) {
        return technologyDao.delete(id);
    }

    @Override
    public List<Technology> searchTechnologyByName(String name) {
        return technologyDao.findByName(name);
    }
}