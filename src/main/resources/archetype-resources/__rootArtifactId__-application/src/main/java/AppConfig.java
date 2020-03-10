package ${package};

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(value = "${groupId}.${artifactId}")
@EnableAspectJAutoProxy
@EnableCaching
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public CacheManager cacheManager() {
        return new org.springframework.cache.support.NoOpCacheManager();
    }

}