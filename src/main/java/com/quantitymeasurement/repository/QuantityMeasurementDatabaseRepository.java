package com.quantitymeasurement.repository;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.quantitymeasurement.exception.DatabaseException;
import com.quantitymeasurement.util.ConnectionPool;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;


public class QuantityMeasurementDatabaseRepository
        implements IQuantityMeasurementRepository
{

    private static final Logger logger =
        Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());

    private static QuantityMeasurementDatabaseRepository instance;

    
    private static final String INSERT_QUERY =
        "INSERT INTO quantity_measurement_entity " +
        "(this_value, this_unit, this_measurement_type, that_value, that_unit, " +
        "that_measurement_type, operation, result_value, result_unit, " +
        "result_measurement_type, result_string, is_error, error_message, " +
        "created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    
    private static final String SELECT_ALL_QUERY =
        "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

    
    private static final String SELECT_BY_OPERATION =
        "SELECT * FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC";

    
    private static final String SELECT_BY_MEASUREMENT_TYPE =
        "SELECT * FROM quantity_measurement_entity " +
        "WHERE this_measurement_type = ? ORDER BY created_at DESC";

    
    private static final String DELETE_ALL_QUERY =
        "DELETE FROM quantity_measurement_entity";

    
    private static final String COUNT_QUERY =
        "SELECT COUNT(*) FROM quantity_measurement_entity";

    
    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE IF NOT EXISTS quantity_measurement_entity (" +
        "  id BIGINT AUTO_INCREMENT PRIMARY KEY," +
        "  this_value DOUBLE NOT NULL," +
        "  this_unit VARCHAR(50) NOT NULL," +
        "  this_measurement_type VARCHAR(50) NOT NULL," +
        "  that_value DOUBLE," +
        "  that_unit VARCHAR(50)," +
        "  that_measurement_type VARCHAR(50)," +
        "  operation VARCHAR(20) NOT NULL," +
        "  result_value DOUBLE," +
        "  result_unit VARCHAR(50)," +
        "  result_measurement_type VARCHAR(50)," +
        "  result_string VARCHAR(255)," +
        "  is_error BOOLEAN DEFAULT FALSE," +
        "  error_message VARCHAR(500)," +
        "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ")";

    private ConnectionPool connectionPool;

    
    private QuantityMeasurementDatabaseRepository() {
        try {
            this.connectionPool = ConnectionPool.getInstance();
            logger.info("QuantityMeasurementDatabaseRepository initialized.");
            
            initializeDatabase();
        } catch (SQLException e) {
            throw DatabaseException.connectionFailed(
                "Failed to initialize connection pool", e);
        }
    }

    
    private void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            stmt.execute(CREATE_TABLE_SQL);
            logger.info("Database schema initialized (table verified or created).");
        } catch (SQLException e) {
            logger.severe("Failed to initialize database schema: " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
    }

    
    public static synchronized QuantityMeasurementDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementDatabaseRepository();
        }
        return instance;
    }

    
    @Override
    public void save(QuantityMeasurementEntity entity) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(INSERT_QUERY);

            
            stmt.setDouble(1,  entity.thisValue);
            stmt.setString(2,  entity.thisUnit);
            stmt.setString(3,  entity.thisMeasurementType);
            stmt.setDouble(4,  entity.thatValue  != null ? entity.thatValue  : 0.0);
            stmt.setString(5,  entity.thatUnit   != null ? entity.thatUnit   : "");
            stmt.setString(6,  entity.thatMeasurementType != null ? entity.thatMeasurementType : "");
            stmt.setString(7,  entity.operation);
            
            if (entity.resultValue != null) {
                stmt.setDouble(8, entity.resultValue);
            } else {
                stmt.setNull(8, Types.DOUBLE);
            }
            stmt.setString(9,  entity.resultUnit);
            stmt.setString(10, entity.resultMeasurementType);
            stmt.setString(11, entity.resultString);
            stmt.setBoolean(12, entity.isError);
            stmt.setString(13, entity.errorMessage);

            stmt.executeUpdate();
            logger.fine("Entity saved to database: operation=" + entity.operation);
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("INSERT entity", e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_QUERY);
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
            logger.info("Retrieved " + results.size() + " measurements from database.");
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("SELECT all measurements", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return results;
    }

   
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_OPERATION);
            stmt.setString(1, operation);
            rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
            logger.fine("Retrieved " + results.size() + " measurements for operation=" + operation);
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("SELECT by operation=" + operation, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return results;
    }

    
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_MEASUREMENT_TYPE);
            stmt.setString(1, measurementType);
            rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
            logger.fine("Retrieved " + results.size() + " measurements for type=" + measurementType);
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("SELECT by type=" + measurementType, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return results;
    }

    
    @Override
    public int getTotalCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(COUNT_QUERY);
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("SELECT COUNT(*)", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    
    @Override
    public void deleteAll() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            stmt.execute(DELETE_ALL_QUERY);
            logger.info("All measurements deleted from database.");
        } catch (SQLException e) {
            throw DatabaseException.queryFailed("DELETE all measurements", e);
        } finally {
            closeResources(stmt, conn);
        }
    }

   
    @Override
    public String getPoolStatistics() {
        return "ConnectionPool{" +
            "available=" + connectionPool.getAvailableConnectionCount() +
            ", used=" + connectionPool.getUsedConnectionCount() +
            ", total=" + connectionPool.getTotalConnectionCount() +
            "}";
    }

    
    @Override
    public void releaseResources() {
        if (connectionPool != null) {
            connectionPool.closeAll();
            logger.info("Database connection pool closed via releaseResources().");
        }
        instance = null;
    }

   
    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        
        double thisValue = rs.getDouble("this_value");
        String thisUnit  = rs.getString("this_unit");
        String thisType  = rs.getString("this_measurement_type");

        double thatValue = rs.getDouble("that_value");
        String thatUnit  = rs.getString("that_unit");
        String thatType  = rs.getString("that_measurement_type");

        String operation = rs.getString("operation");

        
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        entity.thisValue           = thisValue;
        entity.thisUnit            = thisUnit;
        entity.thisMeasurementType = thisType;
        entity.thatValue           = thatValue;
        entity.thatUnit            = thatUnit;
        entity.thatMeasurementType = thatType;
        entity.operation           = operation;
        entity.resultValue         = rs.getObject("result_value") != null
                                     ? rs.getDouble("result_value") : null;
        entity.resultUnit          = rs.getString("result_unit");
        entity.resultMeasurementType = rs.getString("result_measurement_type");
        entity.resultString        = rs.getString("result_string");
        entity.isError             = rs.getBoolean("is_error");
        entity.errorMessage        = rs.getString("error_message");

        return entity;
    }

    
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) {
                logger.warning("Error closing ResultSet: " + e.getMessage());
            }
        }
        closeResources(stmt, conn);
    }

    
    private void closeResources(Statement stmt, Connection conn) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) {
                logger.warning("Error closing Statement: " + e.getMessage());
            }
        }
        if (conn != null) {
            connectionPool.releaseConnection(conn);
        }
    }

    
    public static void main(String[] args) {
        try {
            QuantityMeasurementDatabaseRepository repo =
                QuantityMeasurementDatabaseRepository.getInstance();
            logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName())
                  .info("Pool stats: " + repo.getPoolStatistics());
            logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName())
                  .info("Total records: " + repo.getTotalCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
