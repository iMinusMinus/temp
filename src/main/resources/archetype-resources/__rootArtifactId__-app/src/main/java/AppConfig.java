package ${package};

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(value = "${package}")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AppConfig {


}