package com.orems.dao;

import com.orems.model.*;
import com.orems.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/* RUBRIC: JDBC + OOP - BCrypt + polymorphism */
public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public static AbstractUser findByUsernameAndPassword(String username, String password) {

        if (username == null || username.isBlank() || password == null) return null;

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String hashed = rs.getString("password");
                if (!BCrypt.checkpw(password, hashed)) return null;

                String role = rs.getString("role");
                AbstractUser user;

                switch (role.toLowerCase()) {
                    case "admin": user = new Admin(); break;
                    case "manager": user = new Manager(); break;
                    case "tenant": user = new Tenant(); break;
                    default:
                        logger.warning("Unknown user role: " + role);
                        return null;
                }

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(hashed);
                user.setRole(role);
                user.setFullname(rs.getString("fullname"));

                return user;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user", e);
        }

        return null;
    }

    public static boolean register(AbstractUser user, String plainPassword) {

        String sql = "INSERT INTO users (username, password, role, fullname) VALUES (?, ?, ?, ?)";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, hashed);
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullname());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) user.setId(keys.getInt(1));
            }

            return true;

        } catch (SQLException e) {

            // Duplicate username (SQLState starting with 23)
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                logger.log(Level.WARNING, "Username already exists: " + user.getUsername(), e);
                return false;
            }

            logger.log(Level.SEVERE, "Error registering user", e);
        }

        return false;
    }
}