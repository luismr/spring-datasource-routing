package dev.luismachadoreis.blueprint.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Routing datasource for read-write database operations.
 * 
 * @author Luis Machado Reis
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final DataSource writer;
    private final DataSource reader;

    private boolean readerHealthy = true;
    private long lastCheck = 0;
    private final long checkIntervalMs = 10_000;

    /**
     * Constructor for the RoutingDataSource.
     * 
     * @param writer The writer datasource.
     * @param reader The reader datasource.
     */
    public RoutingDataSource(DataSource writer, DataSource reader) {
        this.writer = writer;
        this.reader = reader;

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbContextHolder.WRITE, writer);
        targetDataSources.put(DbContextHolder.READ, reader);

        this.setTargetDataSources(targetDataSources);
        this.setDefaultTargetDataSource(writer);
        this.afterPropertiesSet();
    }

    /**
     * Determine the current lookup key for the datasource.
     * 
     * @return The current lookup key.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dbType = DbContextHolder.getDbType();

        if (DbContextHolder.READ.equals(dbType)) {
            if (shouldCheckHealth() && !isReaderAvailable()) {
                readerHealthy = false;
                lastCheck = System.currentTimeMillis();

                return DbContextHolder.WRITE;
            }

            return readerHealthy ? DbContextHolder.READ : shouldReturnWriter();
        }

        return shouldReturnWriter();
    }

    /**
     * Determine if the writer datasource should be returned.
     * 
     * @return The writer datasource.
     */
    private String shouldReturnWriter() {
        if (shouldCheckHealth() && !isWriterAvailable()) {
            throw new IllegalStateException("Writer datasource is not available");
        }

        return DbContextHolder.WRITE;
    }

    /**
     * Determine if the health check should be performed.
     * 
     * @return Whether the health check should be performed.
     */    
    private boolean shouldCheckHealth() {
        return System.currentTimeMillis() - lastCheck > checkIntervalMs;
    }

    /**
     * Determine if the datasource is available.
     * 
     * @param dataSource The datasource to check.
     * @return Whether the datasource is available.
     */
    private boolean isDataSourceAvailable(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Determine if the reader datasource is available.
     * 
     * @return Whether the reader datasource is available.
     */ 
    private boolean isReaderAvailable() {
        return isDataSourceAvailable(reader);
    }

    /**
     * Determine if the writer datasource is available.
     * 
     * @return Whether the writer datasource is available.
     */
    private boolean isWriterAvailable() {
        return isDataSourceAvailable(writer);
    }

}
