package me.stevenkin.alohajob.server.repository;

import me.stevenkin.alohajob.server.model.AppDo;
import org.springframework.jdbc.core.JdbcTemplate;

public class AppRepository {
    private JdbcTemplate jdbcTemplate;

    public AppRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AppDo findAppByUsernameAndAppName(String username, String appName) {
        return null;
    }

    public AppDo findAppById(Long id) {
        return null;
    }

    public int updateApp(AppDo appDo) {
        return 0;
    }
}
