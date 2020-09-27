package me.stevenkin.alohajob.common.extension;

import lombok.Setter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExtensionLoader<T> {
    private static final String PREFIX = "META-INF/alohajob/";

    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaderCache = new ConcurrentHashMap<>();
    @Setter
    private static ApplicationContext applicationContext;

    private ConcurrentMap<String, T> instanceCache;

    private ConcurrentMap<String, Class<T>> extensionClassCache;

    private Class<T> type;

    private String defaultExtName;

    private ClassLoader classLoader;

    private volatile boolean inited = false;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        this.defaultExtName = type.getAnnotation(Spi.class).value();
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        checkSpi(type);
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) extensionLoaderCache.get(type);
        if (extensionLoader != null)
            return extensionLoader;
        synchronized (extensionLoaderCache) {
            extensionLoader = (ExtensionLoader<T>) extensionLoaderCache.get(type);
            if (extensionLoader != null)
                return extensionLoader;
            extensionLoader = new ExtensionLoader<>(type);
            extensionLoaderCache.put(type, extensionLoader);
        }
        return extensionLoader;
    }

    private static <T> void checkSpi(Class<T> type) {
        if (!type.isInterface() || !type.isAnnotationPresent(Spi.class))
            throw new RuntimeException("error spi interface");
    }

    public T getExtension(String name) {
        init();
        if (StringUtils.isEmpty(name)) {
            name = defaultExtName;
        }
        if (!extensionClassCache.containsKey(name))
            return null;
        T instance = instanceCache.get(name);
        if (instance != null)
            return instance;
        Class<T> clazz = extensionClassCache.get(name);
        synchronized (instanceCache) {
            instance = instanceCache.get(name);
            if (instance == null) {
                try {
                    instance = (T) parseClassToSpringBean(name, clazz);
                    instanceCache.put(name, instance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return instance;
    }

    private void init() {
        if (!inited)
            loadExtensionClasses();
    }

    private synchronized void loadExtensionClasses() {
        if (inited)
            return;
        extensionClassCache = new ConcurrentHashMap<>();
        instanceCache = new ConcurrentHashMap<>();
        loadExtensionClasses(extensionClassCache);
        inited = true;
    }

    private void loadExtensionClasses(ConcurrentMap<String, Class<T>> extensionClassCache) {
        String fullName = PREFIX + type.getName();
        List<String> classNames = new ArrayList<String>();
        try {
            Enumeration<URL> urls;
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(fullName);
            } else {
                urls = classLoader.getResources(fullName);
            }
            if (urls == null || !urls.hasMoreElements()) {
                return;
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                parseUrl(url, classNames);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loadExtensionClasses(classNames, extensionClassCache);
    }

    private void parseUrl(URL url, List<String> classNames) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line = null;
            int indexNumber = 0;

            while ((line = reader.readLine()) != null) {
                indexNumber++;
                parseLine(line, indexNumber, classNames);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void parseLine(String line, int indexNumber, List<String> classNames) {
        int ci = line.indexOf('#');
        if (ci >= 0) {
            line = line.substring(0, ci);
        }
        line = line.trim();
        if (line.length() <= 0) {
            return;
        }
        if (!classNames.contains(line))
            classNames.add(line);
    }

    private void loadExtensionClasses(List<String> classNames, ConcurrentMap<String, Class<T>> extensionClassCache) {
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (!type.isAssignableFrom(clazz))
                    continue;
                SpiImp spiImp = clazz.getAnnotation(SpiImp.class);
                if (spiImp == null)
                    continue;
                String name = StringUtils.isEmpty(spiImp.name()) ? clazz.getSimpleName() : spiImp.name();
                if (extensionClassCache.containsKey(name))
                    throw new RuntimeException("spiName " + name + " already exist ");
                extensionClassCache.put(name, (Class<T>) clazz);
            } catch (ClassNotFoundException e) {
            }
        }
    }

    private Object parseClassToSpringBean(String name, Class<?> obj) {
        String beanName = obj.getCanonicalName().concat(name);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(obj);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        getRegistry().registerBeanDefinition(beanName, definition);

        return applicationContext.getBean(beanName);
    }

    private BeanDefinitionRegistry getRegistry() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        return (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
    }
}
