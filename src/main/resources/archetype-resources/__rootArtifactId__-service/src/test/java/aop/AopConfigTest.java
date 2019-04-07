package ${package}.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

@Configuration
public class AopConfigTest {

    @Bean(name = "cglibProxiedTarget")
    public TargetInterfaceTest getProxyFromCGLIB(TargetInterfaceTest targetObj) {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setProxyTargetClass(true);
        factoryBean.setTarget(targetObj);
//        factoryBean.addInterceptor(regexpAdvisor);
        return (TargetInterfaceTest) factoryBean.getObject();
    }

    @Bean(name = "jdkProxiedTarget")
    public TargetInterfaceTest getProxyFromJDK(TargetInterfaceTest targetObj) {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setProxyTargetClass(false);
        factoryBean.setTarget(targetObj);
        factoryBean.addInterface(TargetInterfaceTest.class);
//        factoryBean.addInterceptor();
        return (TargetInterfaceTest) factoryBean.getObject();
    }

    @Bean(name = "regexpAdvisor")
    public PointcutAdvisor regexpAdvisor() {
        RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
        advisor.setPattern("*");
//        advisor.setAdvice();
        return advisor;
    }
}