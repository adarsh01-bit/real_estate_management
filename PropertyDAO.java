package com.orems.dao;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.orems.model.Property;
import com.orems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* RUBRIC: JDBC - CRUD operations */
public class PropertyDAO {

    private static final Logger logger = Logger.getLogger(PropertyDAO.class.getName());

    public static List<Property> listAll() {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setAddress(rs.getString("address"));
                p.setRent(rs.getDouble("rent"));

                int mid = rs.getInt("manager_id");
                p.setManagerId(rs.wasNull() ? null : mid);

                p.setAvailable(rs.getBoolean("is_available"));
                list.add(p);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing properties", e);
        }
        return list;
    }

    public static Property findById(int id) {
        String sql = "SELECT * FROM properties WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Property p = new Property();
                    p.setId(rs.getInt("id"));
                    p.setTitle(rs.getString("title"));
                    p.setDescription(rs.getString("description"));
                    p.setAddress(rs.getString("address"));
                    p.setRent(rs.getDouble("rent"));

                    int mid = rs.getInt("manager_id");
                    p.setManagerId(rs.wasNull() ? null : mid);

                    p.setAvailable(rs.getBoolean("is_available"));
                    return p;
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding property by id: " + id, e);
        }
        return null;
    }

    public static boolean add(Property p) {
        if (p == null) throw new IllegalArgumentException("Property cannot be null");

        String sql = "INSERT INTO properties (title, description, address, rent, manager_id, is_available) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getAddress());
            ps.setDouble(4, p.getRent());

            if (p.getManagerId() != null)
                ps.setInt(5, p.getManagerId());
            else
                ps.setNull(5, Types.INTEGER);

            ps.setBoolean(6, p.isAvailable());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }

            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding property: " + p, e);
        }
        return false;
    }
}