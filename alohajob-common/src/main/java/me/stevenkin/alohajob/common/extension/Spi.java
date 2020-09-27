package me.stevenkin.alohajob.common.extension;

import java.lang.annotation.*;

/**
 * 扩展点标记注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {
    /**
     * 默认扩展点名
     * @return
     */
    String value();
}
