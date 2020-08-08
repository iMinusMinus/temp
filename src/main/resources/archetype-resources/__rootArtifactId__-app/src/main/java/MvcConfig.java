package ${package};

#if(!$configType.contains('xml'))
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
#end
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

#if(!$configType.contains('xml'))
@Configuration
@ComponentScan(value = "${package}")
@EnableWebMvc
#end
public class MvcConfig extends WebMvcConfigurationSupport {

}