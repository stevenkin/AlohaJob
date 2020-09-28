package me.stevenkin.alohajob.server.lock;

import me.stevenkin.alohajob.server.model.AppDo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
public class MysqlLockService implements LockService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Lock getLock(String name) {
        jdbcTemplate.queryForObject("select * from app where id = ?", new Object[]{Integer.parseInt(name)}, (rs, i) ->
            new AppDo(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getDate(6))
        );
        return new Lock() {
            private Connection connection;
            {
                try {
                    connection = jdbcTemplate.getDataSource().getConnection();
                    connection.setAutoCommit(false);
                    TransactionSynchronizationManager.bindResource(jdbcTemplate.getDataSource(), new ConnectionHolder(connection));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void lock() {
                jdbcTemplate.queryForObject("select * from app where id = ? for update", new Object[]{Integer.parseInt(name)}, (rs, i) ->
                        new AppDo(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getDate(6))
                );
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean tryLock() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void unlock() {
                try {
                    connection.commit();
                    connection.setAutoCommit(true);
                    DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @NotNull
            @Override
            public Condition newCondition() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
