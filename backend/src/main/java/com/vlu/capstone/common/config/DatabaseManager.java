package com.vlu.capstone.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton-style access to DataSource for connection management.
 * In practice Spring's DataSource is already a pool/singleton; this provides a clear access point.
 */
@Component
@RequiredArgsConstructor
public class DatabaseManager {

    private final DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
