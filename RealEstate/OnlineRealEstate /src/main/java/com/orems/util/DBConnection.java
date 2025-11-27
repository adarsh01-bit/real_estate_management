package com.orems.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DBConnection {
    private static HikariDataSource ds;

    static {
        try {
            // Load MySQL driver explicitly (required)
            Class.forName("com.mysql.cj.jdbc.Driver");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/orems?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            config.setUsername("root");
            config.setPassword("Root@12345");

            config.setDriverClassName("com.mysql.cj.jdbc.Driver");  // ← MANDATORY FIX

            config.setMaximumPoolSize(10);
            config.setPoolName("orems-pool");

            ds = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static DataSource getDataSource() {
        return ds;
    }
}
