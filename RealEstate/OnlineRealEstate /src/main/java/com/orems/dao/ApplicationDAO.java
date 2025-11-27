package com.orems.dao;

import com.orems.model.Application;
import com.orems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* RUBRIC: JDBC - transaction handling when accepting application */
public class ApplicationDAO {

    public static boolean apply(Application app) {
        String sql = "INSERT INTO applications (property_id, tenant_id, message) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, app.getPropertyId());
            ps.setInt(2, app.getTenantId());
            ps.setString(3, app.getMessage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static List<Application> listByManager(int managerId) {
        String sql = "SELECT a.* FROM applications a JOIN properties p ON a.property_id = p.id WHERE p.manager_id = ?";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application a = new Application();
                a.setId(rs.getInt("id"));
                a.setPropertyId(rs.getInt("property_id"));
                a.setTenantId(rs.getInt("tenant_id"));
                a.setMessage(rs.getString("message"));
                a.setStatus(rs.getString("status"));
                a.setAppliedAt(rs.getTimestamp("applied_at"));
                list.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static List<Application> listAll() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application a = new Application();
                a.setId(rs.getInt("id"));
                a.setPropertyId(rs.getInt("property_id"));
                a.setTenantId(rs.getInt("tenant_id"));
                a.setMessage(rs.getString("message"));
                a.setStatus(rs.getString("status"));
                a.setAppliedAt(rs.getTimestamp("applied_at"));
                list.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Accept application: transactionally create rental_agreement and update app.status
    public static boolean acceptApplication(int appId, int managerId, String startDate, String endDate) {
        String getApp = "SELECT * FROM applications WHERE id = ?";
        String updateApp = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(getApp)) {
                ps.setInt(1, appId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) { con.rollback(); return false; }
                int propertyId = rs.getInt("property_id");
                int tenantId = rs.getInt("tenant_id");
                // get rent from property
                double rent = 0.0;
                try (PreparedStatement p2 = con.prepareStatement("SELECT rent FROM properties WHERE id = ?")) {
                    p2.setInt(1, propertyId);
                    ResultSet r2 = p2.executeQuery();
                    if (r2.next()) rent = r2.getDouble(1);
                }
                // create agreement
                boolean created = AgreementDAO.createAgreement(con, propertyId, tenantId, managerId, startDate, endDate, rent);
                if (!created) { con.rollback(); return false; }
                // update application status
                try (PreparedStatement pu = con.prepareStatement(updateApp)) {
                    pu.setString(1, "accepted");
                    pu.setInt(2, appId);
                    pu.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updateStatus(int appId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
