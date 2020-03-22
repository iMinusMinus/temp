package ${package};

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(value = "${package}")
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurationSupport {

}