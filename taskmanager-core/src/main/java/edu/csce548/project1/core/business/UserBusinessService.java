package edu.csce548.project1.core.business;

import edu.csce548.project1.core.dao.UserDAO;
import edu.csce548.project1.core.model.User;

import java.util.List;

/*
 * Business layer facade for user operations.
 * Every CRUD method in UserDAO is exposed here.
 */
public class UserBusinessService {

    private final UserDAO userDAO;

    public UserBusinessService() {
        this.userDAO = new UserDAO();
    }

    public User registerUser(User user) throws Exception {
        return userDAO.create(user);
    }

    public User fetchUser(int id) throws Exception {
        return userDAO.findById(id);
    }

    public List<User> fetchAllUsers() throws Exception {
        return userDAO.findAll();
    }

    public boolean changeUser(User user) throws Exception {
        return userDAO.update(user);
    }

    public boolean removeUser(int id) throws Exception {
        return userDAO.delete(id);
    }
}