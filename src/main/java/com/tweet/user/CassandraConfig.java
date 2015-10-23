package com.tweet.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.xnio.Cancellable;

@Configuration
@EnableCassandraRepositories
public class CassandraConfig {

    @Value("${cassandra.contactpoints:localhost}")
    private String contactpoints;

    @Value("${cassandra.port:9042}")
    private Integer port;

    @Value("${cassandra.keyspace:user}")
    private String keyspace;

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactpoints);
        cluster.setPort(port);
        return cluster;
    }

    @Bean
    public CassandraMappingContext cassandraMapping() {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraConverter converter(CassandraMappingContext cassandraMappingContext) {
        return new MappingCassandraConverter(cassandraMappingContext);
    }

    @Bean
    public CassandraSessionFactoryBean session(CassandraConverter cassandraConverter, CassandraClusterFactoryBean cluster) throws Exception {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster.getObject());
        session.setKeyspaceName(keyspace);
        session.setConverter(cassandraConverter);
        session.setSchemaAction(SchemaAction.NONE);
        return session;
    }

    @Bean
    public CassandraOperations cassandraTemplate(CassandraSessionFactoryBean factoryBean) throws Exception {
        return new CassandraTemplate(factoryBean.getObject());
    }

}