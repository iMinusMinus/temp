package ${package};

#if($framework.contains("dubbo"))
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
#end
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

#if($showComment)
// @EnableDubbo register ReferenceAnnotationBeanPostProcessor, DubboConfigAliasPostProcessor, DubboApplicationListenerRegistrar,
// DubboConfigDefaultPropertyValueBeanPostProcessor, DubboConfigEarlyInitializationPostProcessor
#end
@Configuration
#if($framework.contains("dubbo") and !$configType.contains("xml"))
@EnableDubbo
#end
public class RpcConfig {
#if($framework.contains("dubbo") and !$configType.contains("xml"))

    @Value("${spring.application.name}")
    private String name;

    #[[@Value("${dubbo.registry.addresss:N/A}")]]#
    private String address;

    @Bean
    public ApplicationConfig ${dubboApplicationConfigBeanName}() {
        ApplicationConfig applicationConfig = new ApplicationConfig(name);
        applicationConfig.setRegistryIds("${dubboRegistryConfigBeanName}");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig ${dubboRegistryConfigBeanName}() {
        return new RegistryConfig(address);
    }
#end
}