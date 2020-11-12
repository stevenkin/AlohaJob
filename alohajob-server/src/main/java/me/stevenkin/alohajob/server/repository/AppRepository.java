package me.stevenkin.alohajob.server.repository;

import me.stevenkin.alohajob.server.model.AppDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
