package ru.kuzmichev.SimpleBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.kuzmichev.SimpleBank")
@PropertySource("classpath:test-application.properties")
@EnableTransactionManagement
public class TestDatasourceConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("Test DB " + Math.random())
                .setType(H2)
                .setScriptEncoding("UTF-8");

        return builder.build();
    }
}
