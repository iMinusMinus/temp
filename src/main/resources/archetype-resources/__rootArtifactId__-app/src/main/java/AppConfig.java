package ${package};

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {

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