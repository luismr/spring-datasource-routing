package dev.luismachadoreis.blueprint.datasource;

/**
 * Thread-local holder for database context.
 * 
 * @author Luis Machado Reis
 */
public class DbContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static final String READ = "READ";
    public static final String WRITE = "WRITE";

    /**
     * Set the database type in the context.
     * 
     * @param dbType The database type.
     */
    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    /**
     * Get the database type from the context.
     * 
     * @return The database type.
     */ 
    public static String getDbType() {
        return contextHolder.get();
    }

    /**
     * Clear the database type from the context.
     */
    public static void clearDbType() {
        contextHolder.remove();
    }

}
