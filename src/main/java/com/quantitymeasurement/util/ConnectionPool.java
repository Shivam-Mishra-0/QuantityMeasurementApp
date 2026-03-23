package com.quantitymeasurement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ConnectionPool {

    
    private static final Logger logger = Logger.getLogger(
        ConnectionPool.class.getName()
    );

    private static ConnectionPool instance;

    private List<Connection> availableConnections;

    private List<Connection> usedConnections;

    private final int poolSize;

    private final String dbUrl;

    private final String dbUsername;

    private final String dbPassword;

    private final String driverClass;

    private final String testQuery;

   
    private ConnectionPool() throws SQLException {
        ApplicationConfig config = ApplicationConfig.getInstance();

        
        this.dbUrl        = config.getProperty("db.url",      "jdbc:h2:./data/quantitymeasurementdb;AUTO_SERVER=TRUE");
        this.dbUsername   = config.getProperty("db.username", "sa");
        this.dbPassword   = config.getProperty("db.password", "");
        this.driverClass  = config.getProperty("db.driver",   "org.h2.Driver");
        this.testQuery    = config.getProperty("db.hikari.connection-test-query", "SELECT 1");
        this.poolSize     = config.getIntProperty("db.pool-size", 5);

        this.availableConnections = new ArrayList<>();
        this.usedConnections      = new ArrayList<>();

        
        try {
            Class.forName(driverClass);
            logger.info("JDBC driver loaded: " + driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC driver not found: " + driverClass, e);
        }

        
        initializeConnections();
        logger.info("ConnectionPool initialized with " + poolSize + " connections.");
    }

    
    public static synchronized ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    
    private void initializeConnections() throws SQLException {
        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createConnection());
        }
    }

    
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    
    public synchronized Connection getConnection() throws SQLException {
        
        if (!availableConnections.isEmpty()) {
            Connection conn = availableConnections.remove(0);
            
            if (!validateConnection(conn)) {
                logger.warning("Stale connection detected — creating fresh replacement");
                conn = createConnection();
            }
            usedConnections.add(conn);
            logger.fine("Connection acquired. Available: " + availableConnections.size()
                + ", Used: " + usedConnections.size());
            return conn;
        }

        
        if (usedConnections.size() < poolSize) {
            Connection conn = createConnection();
            usedConnections.add(conn);
            logger.info("New connection created on demand. Used: " + usedConnections.size());
            return conn;
        }

        
        throw new SQLException(
            "Connection pool exhausted. All " + poolSize + " connections are in use."
        );
    }

    
    public synchronized void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
       
        usedConnections.remove(connection);
        availableConnections.add(connection);
        logger.fine("Connection released. Available: " + availableConnections.size()
            + ", Used: " + usedConnections.size());
    }

    
    public boolean validateConnection(Connection connection) {
        try (var stmt = connection.createStatement()) {
            stmt.execute(this.testQuery);
            return true;
        } catch (SQLException e) {
            logger.warning("Connection validation failed: " + e.getMessage());
            return false;
        }
    }

    
    public synchronized void closeAll() {
        
        for (Connection conn : availableConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warning("Error closing available connection: " + e.getMessage());
            }
        }
        availableConnections.clear();

        
        for (Connection conn : usedConnections) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warning("Error closing used connection: " + e.getMessage());
            }
        }
        usedConnections.clear();

        
        instance = null;
        logger.info("All connections closed. Connection pool shut down.");
    }

    
    public int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    
    public int getUsedConnectionCount() {
        return usedConnections.size();
    }

    
    public int getTotalConnectionCount() {
        return availableConnections.size() + usedConnections.size();
    }

    
    @Override
    public String toString() {
        return "ConnectionPool{"
            + "poolSize=" + poolSize
            + ", available=" + availableConnections.size()
            + ", used=" + usedConnections.size()
            + "}";
    }

    
    public static void main(String[] args) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn1 = pool.getConnection();
            logger.getLogger(ConnectionPool.class.getName()).info(
                "Validate connection: " + (pool.validateConnection(conn1) ? "Success" : "Failure"));
            logger.getLogger(ConnectionPool.class.getName()).info(
                "Available connections after acquiring 1: " + pool.getAvailableConnectionCount());
            logger.getLogger(ConnectionPool.class.getName()).info(
                "Used connections after acquiring 1: " + pool.getUsedConnectionCount());
            pool.releaseConnection(conn1);
            logger.getLogger(ConnectionPool.class.getName()).info(
                "Available connections after releasing 1: " + pool.getAvailableConnectionCount());
            logger.getLogger(ConnectionPool.class.getName()).info(
                "Used connections after releasing 1: " + pool.getUsedConnectionCount());
            pool.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}