package dev.luismachadoreis.blueprint.datasource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.Map;
import java.util.HashMap;
import javax.sql.DataSource;

/**
 * Configuration for datasources.
 * 
 * @author Luis Machado Reis
 */
@Configuration
@EnableConfigurationProperties(ReadWriteRoutingProperties.class)
public class DataSourceConfig {

    /**
     * Writer datasource.
     * 
     * @return The writer datasource.
     */
    @Bean(name = "writerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.writer")
    public DataSource writerDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Reader datasource.
     * 
     * @return The reader datasource.
     */
    @Bean(name = "readerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.reader")
    public DataSource readerDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Data source bean.
     * 
     * @param writer The writer datasource.
     * @param reader The reader datasource.
     * @param props The read-write routing properties.
     * @return The data source.
     */
    @Bean
    @Primary
    public DataSource dataSource(
            @Qualifier("writerDataSource") DataSource writer,
            @Qualifier("readerDataSource") DataSource reader,
            ReadWriteRoutingProperties props
    ) {
        if (!props.isEnabled()) {
            return writer;
        }

        RoutingDataSource routingDataSource = new RoutingDataSource(writer, reader);

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DbContextHolder.WRITE, writer);
        dataSources.put(DbContextHolder.READ, reader);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writer);

        return routingDataSource;
    }

}
