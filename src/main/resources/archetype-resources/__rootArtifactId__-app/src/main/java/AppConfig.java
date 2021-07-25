package ${package};

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AppConfig {


    // no quartz configuration here, use xxl-job, elasticjob instead!
}