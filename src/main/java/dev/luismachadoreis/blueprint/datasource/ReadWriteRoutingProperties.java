package dev.luismachadoreis.blueprint.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for read-write routing.
 * 
 * @author Luis Machado Reis
 */
@ConfigurationProperties(prefix = "app.read-write-routing")
public class ReadWriteRoutingProperties {

    private boolean enabled;

    /**
     * Whether read-write routing is enabled.
     * 
     * @return Whether read-write routing is enabled.
     */ 
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set whether read-write routing is enabled.
     * 
     * @param enabled Whether read-write routing is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}