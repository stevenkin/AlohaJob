package me.stevenkin.alohajob.server.config;

import me.stevenkin.alohajob.server.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfiguration {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Bean
    public AppRepository appRepository() {
        return new AppRepository(jdbcTemplate);
    }
}
