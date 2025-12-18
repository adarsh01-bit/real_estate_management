package com.orems.dao;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/* RUBRIC: JDBC - transaction helper */
public class AgreementDAO {

    private static final Logger logger = Logger.getLogger(AgreementDAO.class.getName());

    public static boolean createAgreement(Connection con,
                                          int propertyId,
                                          int tenantId,
                                          int managerId,
                                          String startDate,
                                          String endDate,
                                          double rent) throws SQLException {

        String sql =
                "INSERT INTO rental_agreements (property_id, tenant_id, manager_id, start_date, end_date, rent) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, propertyId);
            ps.setInt(2, tenantId);
            ps.setInt(3, managerId);

            // safe date parsing
            try {
                if (startDate != null && !startDate.isBlank())
                    ps.setDate(4, Date.valueOf(startDate));
                else
                    ps.setNull(4, Types.DATE);

                if (endDate != null && !endDate.isBlank())
                    ps.setDate(5, Date.valueOf(endDate));
                else
                    ps.setNull(5, Types.DATE);
            } catch (IllegalArgumentException ex) {
                throw new SQLException("Invalid date format", ex);
            }

            ps.setDouble(6, rent);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating agreement", e);
            throw e; // important: let ApplicationDAO handle rollback
        }
    }
}