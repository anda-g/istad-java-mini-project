package model.repository;

import model.dto.UserLoginDto;
import model.entites.User;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User, Integer> {
    @Override
    public List<User> findAll() {
        String sql = """
                SELECT * FROM users
                WHERE is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(getUser(rs));
            }
            return users;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User findById(Integer id) {
        String sql = """
                SELECT * FROM users
                WHERE id = ? AND is_deleted = FALSE
                """;
        try(Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User findByUuid(String uuid) {
        String sql = """
                SELECT * FROM users
                WHERE uuid = ? AND is_deleted = FALSE
                """;
        return findUser(uuid, sql);
    }

    @Override
    public Boolean save(User user) {
        User checkUser = findByEmail(user.getEmail());
        if(checkUser != null) {
            System.err.println("[!] Email already exists!");
            return false;
        }else{
            checkUser = findByUsername(user.getUsername());
            if(checkUser != null) {
                System.err.println("[!] Username already exists!");
                return false;
            }
        }

        String sql = """
                INSERT INTO users (uuid, username, email, password, is_deleted, role)
                VALUES ( ?, ?, ?, ?, FALSE, ?)
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUuid());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole());
            if(stmt.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteById(Integer id) {
        String sql = """
                UPDATE users
                SET is_deleted = TRUE
                WHERE id = ?
                """;
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if(ps.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean deleteByUuid(String uuid) {
        User user = findByUuid(uuid);
        if(user != null){
            deleteById(user.getId());
            return true;
        }
        return false;
    }

    public User findByEmail(String email) {
        String sql = """
                SELECT * FROM users
                WHERE email = ? AND is_deleted = FALSE
                """;
        return findUser(email, sql);
    }

    public User findByUsername(String username) {
        String sql = """
                SELECT * FROM users
                WHERE username = ? AND is_deleted = FALSE
                """;
        return findUser(username, sql);
    }

    private User findUser(String iden, String sql) {
        try(Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, iden);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getUser(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("[!] SQLException: " + e.getMessage());
        }
        return null;
    }

    public User authUser(UserLoginDto userLoginDto){
        User user = findByEmail(userLoginDto.email());
        if(user != null){
            if(user.getPassword().equals(userLoginDto.password())){
                return user;
            }
        }
        return null;
    }

    public User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUuid(rs.getString("uuid"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
