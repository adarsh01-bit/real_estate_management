package com.orems.dao;

import com.orems.model.Property;
import com.orems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* RUBRIC: JDBC - CRUD operations */
public class PropertyDAO {
    public static List<Property> listAll() {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setAddress(rs.getString("address"));
                p.setRent(rs.getDouble("rent"));
                int mid = rs.getInt("manager_id"); if (rs.wasNull()) p.setManagerId(null); else p.setManagerId(mid);
                p.setAvailable(rs.getBoolean("is_available"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Property findById(int id) {
        String sql = "SELECT * FROM properties WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Property p = new Property();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setDescription(rs.getString("description"));
                p.setAddress(rs.getString("address"));
                p.setRent(rs.getDouble("rent"));
                int mid = rs.getInt("manager_id"); if (rs.wasNull()) p.setManagerId(null); else p.setManagerId(mid);
                p.setAvailable(rs.getBoolean("is_available"));
                return p;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean add(Property p) {
        String sql = "INSERT INTO properties (title, description, address, rent, manager_id, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getAddress());
            ps.setDouble(4, p.getRent());
            if (p.getManagerId() != null) ps.setInt(5, p.getManagerId()); else ps.setNull(5, Types.INTEGER);
            ps.setBoolean(6, p.isAvailable());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
