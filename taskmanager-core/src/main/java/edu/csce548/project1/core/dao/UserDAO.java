package edu.csce548.project1.core.dao;

import edu.csce548.project1.core.db.DBUtil;
import edu.csce548.project1.core.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * UserDAO provides CRUD methods for users table.
 */
public class UserDAO {

    public User create(User user) throws Exception {
        String sql = "INSERT INTO users (name) VALUES (?)";
        try (Connection c = DBUtil.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.userId = rs.getInt(1);
                }
            }
        }
        return user;
    }

    public User findById(int id) throws Exception {
        String sql = "SELECT user_id, name FROM users WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws Exception {
        List<User> out = new ArrayList<>();
        String sql = "SELECT user_id, name FROM users ORDER BY user_id";
        try (Connection c = DBUtil.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new User(rs.getInt("user_id"), rs.getString("name")));
            }
        }
        return out;
    }

    public boolean update(User user) throws Exception {
        String sql = "UPDATE users SET name = ? WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.name);
            ps.setInt(2, user.userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}