package ${package}.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

@Configuration
public class AopConfigTest {

    @Bean
    public TargetInterfaceTest targetBean() {
        return new TargetClassTest();
    }

    @Bean(name = "cglibProxiedTarget")
    public TargetInterfaceTest getProxyFromCGLIB() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setProxyTargetClass(true);
        factoryBean.setTarget(targetBean());
//        factoryBean.addInterceptor(regexpAdvisor);
        return (TargetInterfaceTest) factoryBean.getObject();
    }

    @Bean(name = "jdkProxiedTarget")
    public TargetInterfaceTest getProxyFromJDK() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setProxyTargetClass(false);
        factoryBean.setTarget(targetBean());
        factoryBean.addInterface(TargetInterfaceTest.class);
//        factoryBean.addInterceptor();
        return (TargetInterfaceTest) factoryBean.getObject();
    }

    @Bean(name = "regexpAdvisor")
    public PointcutAdvisor regexpAdvisor() {
        RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
        advisor.setPattern("echo");
//        advisor.setAdvice();
        return advisor;
    }
}