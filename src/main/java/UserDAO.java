import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * UserDAO: simple CRUD - create, findById, findAll, update, delete.
 */
public class UserDAO {
    public User create(User u) throws Exception {
        String sql = "INSERT INTO users (name) VALUES (?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.userId = rs.getInt(1);
                }
            }
        }
        return u;
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
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new User(rs.getInt("user_id"), rs.getString("name")));
            }
        }
        return out;
    }

    public boolean update(User u) throws Exception {
        String sql = "UPDATE users SET name = ? WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.name);
            ps.setInt(2, u.userId);
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