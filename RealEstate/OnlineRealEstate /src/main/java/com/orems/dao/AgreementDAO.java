package com.orems.dao;

import com.orems.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* RUBRIC: JDBC - transaction example used by ApplicationDAO */
public class AgreementDAO {
    public static boolean createAgreement(Connection con, int propertyId, int tenantId, int managerId, String startDate, String endDate, double rent) throws SQLException {
        String sql = "INSERT INTO rental_agreements (property_id, tenant_id, manager_id, start_date, end_date, rent) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, propertyId);
            ps.setInt(2, tenantId);
            ps.setInt(3, managerId);
            if (startDate != null) ps.setDate(4, java.sql.Date.valueOf(startDate)); else ps.setNull(4, java.sql.Types.DATE);
            if (endDate != null) ps.setDate(5, java.sql.Date.valueOf(endDate)); else ps.setNull(5, java.sql.Types.DATE);
            ps.setDouble(6, rent);
            return ps.executeUpdate() > 0;
        }
    }
}
