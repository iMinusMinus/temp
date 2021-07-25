package ${package};

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
#if($framework.contains("ehcache") or $framework.contains("hazelcast") or $framework.contains("infinispan"))
import org.springframework.cache.jcache.JCacheCacheManager;
#end
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.ConfigurableApplicationContext;
#if($framework.contains('quartz') and !$configType.contains('xml'))
import org.springframework.scheduling.annotation.EnableScheduling;
#end
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.net.URI;

#if($framework.contains("ehcache") or $framework.contains("hazelcast") or $framework.contains("infinispan"))
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
#end

#if($showComment)
// @EnableAspectJAutoProxy registrer AnnotationAwareAspectJAutoProxyCreator
// @EnableCaching import ProxyCachingConfiguration or AspectJCachingConfiguration
// @EnableScheduling register ScheduledAnnotationBeanPostProcessor. Stop using quartz, xxl-job, elasticjob instead!
// @EnableTransactionManagement import ProxyTransactionManagementConfiguration or AspectJTransactionManagementConfiguration
#end
@Configuration
@Import(value = {AppConfig.class, DalConfig.class, RpcConfig.class, MqConfig.class})
@ComponentScan(value = "${package}",
        excludeFilters = {@ComponentScan.Filter(value = {Controller.class, Configuration.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MvcConfig.class})})
#if(!$configType.contains('xml'))
@EnableAspectJAutoProxy
@EnableCaching
#if($framework.contains('quartz'))
@EnableScheduling
#end
@EnableTransactionManagement
#end
public class ContextConfig implements BeanClassLoaderAware {
#[[
    @Value("${spring.cache.cacheNames:}")
    private String cacheNames;

    @Value("${spring.cache.jcache.provider:}")
    private String provider;

    @Value("${spring.cache.jcache.config:}")
    private String configLocation;
]]#
    private ClassLoader beanClassLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }


    @Bean
    public CacheManager cacheManager() throws Exception {
#if($framework.contains("ehcache") or $framework.contains("hazelcast") or $framework.contains("infinispan"))
        CachingProvider cachingProvider = null;
        if(StringUtils.hasText(provider)) {
            cachingProvider = Caching.getCachingProvider(provider);
        } else {
            cachingProvider = Caching.getCachingProvider();
        }
        URI uri = null;
        if(configLocation != null && !configLocation.isEmpty()) {
            uri = ResourceUtils.toURI(beanClassLoader.getResource(configLocation));
        }
        javax.cache.CacheManager jCacheCacheManager = cachingProvider.getCacheManager(uri, beanClassLoader);
        if(cacheNames != null && !cacheNames.isEmpty()) {
            String[] caches = cacheNames.split(ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for(String cacheName : caches) {
                jCacheCacheManager.createCache(cacheName, new MutableConfiguration());
            }
        }
        return new JCacheCacheManager(jCacheCacheManager);
#else
        // local cache may be use caffeine
        return new NoOpCacheManager();
#end
    }

}