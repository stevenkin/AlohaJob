package me.stevenkin.alohajob.server;

import me.stevenkin.alohajob.server.config.AlohaJobServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AlohaJobServerConfiguration.class)
public @interface EnableAlohaJobServer {

}
