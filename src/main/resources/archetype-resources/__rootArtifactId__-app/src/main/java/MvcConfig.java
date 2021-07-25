package ${package};

#if(!$configType.contains('xml'))
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
#end
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

#if(!$configType.contains('xml'))
@Configuration
@ComponentScan(value = "${package}", excludeFilters = @ComponentScan.Filter(value = {Service.class, Repository.class, Configuration.class}))
@EnableWebMvc
#end
public class MvcConfig extends WebMvcConfigurationSupport {

}