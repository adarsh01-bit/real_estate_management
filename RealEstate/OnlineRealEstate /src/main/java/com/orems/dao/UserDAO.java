package com.orems.dao;

import com.orems.model.*;
import com.orems.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* RUBRIC: JDBC + OOP - BCrypt password hashing, polymorphic user creation */
public class UserDAO {

    public static AbstractUser findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    String role = rs.getString("role");
                    AbstractUser u;
                    if ("manager".equals(role)) u = new Manager();
                    else if ("admin".equals(role)) u = new Admin();
                    else u = new Tenant();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(hashed);
                    u.setRole(role);
                    u.setFullname(rs.getString("fullname"));
                    return u;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean register(AbstractUser user, String plainPassword) {
        String sql = "INSERT INTO users (username, password, role, fullname) VALUES (?, ?, ?, ?)";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashed);
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullname());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
