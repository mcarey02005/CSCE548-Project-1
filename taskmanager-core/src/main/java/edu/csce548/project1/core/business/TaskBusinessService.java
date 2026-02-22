package edu.csce548.project1.core.business;

import edu.csce548.project1.core.dao.TaskDAO;
import edu.csce548.project1.core.model.Task;

import java.util.List;

/*
 * Business layer facade for task operations.
 * Every CRUD method in TaskDAO is exposed here, including list with joined names.
 */
public class TaskBusinessService {

    private final TaskDAO taskDAO;

    public TaskBusinessService() {
        this.taskDAO = new TaskDAO();
    }

    public Task registerTask(Task task) throws Exception {
        return taskDAO.create(task);
    }

    public Task fetchTask(int id) throws Exception {
        return taskDAO.findById(id);
    }

    public List<Task> fetchAllTasks() throws Exception {
        return taskDAO.findAll();
    }

    public List<String> fetchAllTasksWithNames() throws Exception {
        return taskDAO.findAllWithNames();
    }

    public boolean changeTask(Task task) throws Exception {
        return taskDAO.update(task);
    }

    public boolean removeTask(int id) throws Exception {
        return taskDAO.delete(id);
    }
}