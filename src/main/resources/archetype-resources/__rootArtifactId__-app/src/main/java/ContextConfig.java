package ${package};

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;
#if($framework.contains('quartz') and !$configType.contains('xml'))
import org.springframework.scheduling.annotation.EnableScheduling;
#end
import org.springframework.stereotype.Controller;

@Configuration
@Import(value = {AppConfig.class, DalConfig.class, RpcConfig.class, MqConfig.class})
@ComponentScan(value = "${package}",
        excludeFilters = {@ComponentScan.Filter(value = {Controller.class, Configuration.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MvcConfig.class})})
#if($framework.contains('quartz') and !$configType.contains('xml'))
@EnableScheduling
#end
public class ContextConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if(activeProfile == null) {
            activeProfile = System.getenv(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME.replace(".", "_").toUpperCase());
        }
        PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
        placeholder.setLocations(new ClassPathResource("application.properties"), new ClassPathResource("application-" + activeProfile + ".properties"));
        return placeholder;
    }

}