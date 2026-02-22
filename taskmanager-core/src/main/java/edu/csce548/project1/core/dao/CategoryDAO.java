package edu.csce548.project1.core.dao;

import edu.csce548.project1.core.db.DBUtil;
import edu.csce548.project1.core.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public Category create(Category category) throws Exception {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    category.categoryId = rs.getInt(1);
                }
            }
        }
        return category;
    }

    public Category findById(int id) throws Exception {
        String sql = "SELECT category_id, name FROM categories WHERE category_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("category_id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public List<Category> findAll() throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name FROM categories ORDER BY category_id";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("category_id"), rs.getString("name")));
            }
        }
        return list;
    }

    public boolean update(Category category) throws Exception {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.name);
            ps.setInt(2, category.categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}