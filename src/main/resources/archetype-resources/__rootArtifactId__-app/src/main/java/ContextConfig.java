package ${package};

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
#if($framework.contains('quartz') and !$configType.contains('xml'))
import org.springframework.scheduling.annotation.EnableScheduling;
#end
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(value = "${package}", excludeFilters = @ComponentScan.Filter(value = {Controller.class}))
#if($framework.contains('quartz') and !$configType.contains('xml'))
@EnableScheduling
#end
public class ContextConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        String activeProfile = System.getProperty("spring.profiles.active");
        PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
        placeholder.setLocations(new ClassPathResource("application.properties"), new ClassPathResource("application-" + activeProfile + ".properties"));
        return placeholder;
    }

}