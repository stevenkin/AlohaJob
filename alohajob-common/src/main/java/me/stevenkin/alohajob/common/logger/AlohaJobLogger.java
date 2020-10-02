package me.stevenkin.alohajob.common.logger;

public interface AlohaJobLogger {
    void debug(String messagePattern, Object... args);

    void info(String messagePattern, Object... args);

    void warn(String messagePattern, Object... args);

    void error(String messagePattern, Object... args);
}
