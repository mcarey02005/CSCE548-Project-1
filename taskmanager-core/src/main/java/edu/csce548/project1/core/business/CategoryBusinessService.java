package edu.csce548.project1.core.business;

import edu.csce548.project1.core.dao.CategoryDAO;
import edu.csce548.project1.core.model.Category;

import java.util.List;

/*
 * Business layer facade for category operations.
 * Every CRUD method in CategoryDAO is exposed here.
 */
public class CategoryBusinessService {

    private final CategoryDAO categoryDAO;

    public CategoryBusinessService() {
        this.categoryDAO = new CategoryDAO();
    }

    public Category registerCategory(Category category) throws Exception {
        return categoryDAO.create(category);
    }

    public Category fetchCategory(int id) throws Exception {
        return categoryDAO.findById(id);
    }

    public List<Category> fetchAllCategories() throws Exception {
        return categoryDAO.findAll();
    }

    public boolean changeCategory(Category category) throws Exception {
        return categoryDAO.update(category);
    }

    public boolean removeCategory(int id) throws Exception {
        return categoryDAO.delete(id);
    }
}