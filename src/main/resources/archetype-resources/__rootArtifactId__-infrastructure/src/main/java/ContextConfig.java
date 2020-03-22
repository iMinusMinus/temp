package ${package};

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan(value = "${groupId}.${rootArtifactId}")
public class ContextConfig {

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
        placeholder.setLocations(new ClassPathResource("application.properties"), new ClassPathResource("application-" + profile + ".properties"));
        return placeholder;
    }

}