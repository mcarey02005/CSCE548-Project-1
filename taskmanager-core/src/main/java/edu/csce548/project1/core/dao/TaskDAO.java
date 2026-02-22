package edu.csce548.project1.core.dao;

import edu.csce548.project1.core.db.DBUtil;
import edu.csce548.project1.core.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * TaskDAO provides CRUD methods and a read model that joins user/category names.
 */
public class TaskDAO {

    public Task create(Task task) throws Exception {
        String sql = "INSERT INTO tasks (user_id, category_id, description, completed) VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, task.userId);
            ps.setInt(2, task.categoryId);
            ps.setString(3, task.description);
            ps.setBoolean(4, task.completed);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    task.taskId = rs.getInt(1);
                }
            }
        }
        return task;
    }

    public Task findById(int id) throws Exception {
        String sql = "SELECT task_id, user_id, category_id, description, completed FROM tasks WHERE task_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                            rs.getInt("task_id"),
                            rs.getInt("user_id"),
                            rs.getInt("category_id"),
                            rs.getString("description"),
                            rs.getBoolean("completed"));
                }
            }
        }
        return null;
    }

    public List<Task> findAll() throws Exception {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT task_id, user_id, category_id, description, completed FROM tasks ORDER BY task_id";
        try (Connection c = DBUtil.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Task(
                        rs.getInt("task_id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getBoolean("completed")));
            }
        }
        return list;
    }

    public boolean update(Task task) throws Exception {
        String sql = "UPDATE tasks SET user_id=?, category_id=?, description=?, completed=? WHERE task_id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, task.userId);
            ps.setInt(2, task.categoryId);
            ps.setString(3, task.description);
            ps.setBoolean(4, task.completed);
            ps.setInt(5, task.taskId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<String> findAllWithNames() throws Exception {
        List<String> out = new ArrayList<>();
        String sql = "SELECT t.task_id, u.name AS user_name, c.name AS category_name, t.description, t.completed "
                + "FROM tasks t JOIN users u ON t.user_id = u.user_id "
                + "JOIN categories c ON t.category_id = c.category_id ORDER BY t.task_id";
        try (Connection c = DBUtil.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("task_id");
                String user = rs.getString("user_name");
                String cat = rs.getString("category_name");
                String desc = rs.getString("description");
                boolean done = rs.getBoolean("completed");
                out.add(String.format("%d) [%s] (%s) - %s%s", id, user, cat, desc, done ? " [DONE]" : ""));
            }
        }
        return out;
    }
}