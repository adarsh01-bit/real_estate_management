package com.orems.dao;

import com.orems.model.Application;
import com.orems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/* RUBRIC: JDBC - transaction handling when accepting application */
public class ApplicationDAO {

    private static final Logger logger = Logger.getLogger(ApplicationDAO.class.getName());

    public static boolean apply(Application app) {
        String sql = "INSERT INTO applications (property_id, tenant_id, message) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, app.getPropertyId());
            ps.setInt(2, app.getTenantId());
            ps.setString(3, app.getMessage());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error applying for property", e);
        }
        return false;
    }

    public static List<Application> listByManager(int managerId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.* FROM applications a JOIN properties p ON a.property_id = p.id WHERE p.manager_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {
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
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing applications for manager", e);
        }
        return list;
    }

    public static List<Application> listAll() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing applications", e);
        }
        return list;
    }

    public static boolean acceptApplication(int appId, int managerId, String startDate, String endDate) {

        String getApp = "SELECT * FROM applications WHERE id = ?";
        String updateApp = "UPDATE applications SET status = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(getApp)) {

                ps.setInt(1, appId);
                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    int propertyId = rs.getInt("property_id");
                    int tenantId = rs.getInt("tenant_id");

                    // check manager ownership
                    double rent = 0;
                    int ownerId = -1;

                    try (PreparedStatement p2 = con.prepareStatement("SELECT rent, manager_id FROM properties WHERE id = ?")) {
                        p2.setInt(1, propertyId);

                        try (ResultSet r2 = p2.executeQuery()) {
                            if (!r2.next()) {
                                con.rollback();
                                return false;
                            }

                            rent = r2.getDouble("rent");
                            ownerId = r2.getInt("manager_id");

                            if (r2.wasNull()) ownerId = -1;
                        }
                    }

                    if (ownerId != managerId) {
                        con.rollback();
                        return false;
                    }

                    boolean created = AgreementDAO.createAgreement(con, propertyId, tenantId, managerId, startDate, endDate, rent);

                    if (!created) {
                        con.rollback();
                        return false;
                    }

                    try (PreparedStatement pu = con.prepareStatement(updateApp)) {
                        pu.setString(1, "accepted");
                        pu.setInt(2, appId);
                        pu.executeUpdate();
                    }

                    con.commit();
                    return true;
                }

            } catch (SQLException e) {
                con.rollback();
                logger.log(Level.SEVERE, "Error accepting application", e);
                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error in application acceptance transaction", e);
        }

        return false;
    }

    public static boolean updateStatus(int appId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, appId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating application status", e);
        }

        return false;
    }
}