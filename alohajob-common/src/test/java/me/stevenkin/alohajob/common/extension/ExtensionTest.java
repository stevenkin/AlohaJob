package me.stevenkin.alohajob.common.extension;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ExtensionTest {
    @Test
    public void test() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(me.stevenkin.alohajob.common.extension.TestConfig.class);
        ExtensionLoader.setApplicationContext(applicationContext);
        me.stevenkin.alohajob.common.extension.TestService test = ExtensionLoader.getExtensionLoader(me.stevenkin.alohajob.common.extension.TestService.class).getExtension("test2");
        Assert.assertEquals("test2", test.hello());
    }
}
