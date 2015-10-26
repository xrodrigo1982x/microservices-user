package com.tweet.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StreamUtils;
import org.xnio.Cancellable;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
@EnableCassandraRepositories
public class CassandraConfig {

    private Logger LOG = LoggerFactory.getLogger(CassandraConfig.class);

    @Value("${cassandra.hosts:localhost}")
    private String contactpoints;

    @Value("${cassandra.port:9042}")
    private Integer port;

    @Value("${cassandra.keyspace:user}")
    private String keyspace;

    @Bean
    public CassandraClusterFactoryBean cluster() throws Exception {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactpoints);
        cluster.setPort(port);
        init(cluster);
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

    private void init(CassandraClusterFactoryBean cluster) throws Exception {
            String[] cqls = StreamUtils.copyToString(this.getClass().getClassLoader().getResourceAsStream("database.cql"), Charset.defaultCharset()).split("\n");
            cluster.afterPropertiesSet();
            for (String cql : cqls) {
                try {
                    cluster.getObject().connect().execute(cql);
                } catch (Exception e) {
                    LOG.warn(e.getMessage());
                }
            }
    }

    @Bean
    public CassandraOperations cassandraTemplate(CassandraSessionFactoryBean factoryBean) throws Exception {
        return new CassandraTemplate(factoryBean.getObject());
    }

}