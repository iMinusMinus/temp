package ${package}.aop;

import ${package}.BaseAppTest;
import ${package}.AppConfig;

import org.springframework.test.context.ContextConfiguration;

import org.junit.Test;

import javax.annotation.Resource;

@ContextConfiguration(classes = {AppConfig.class, AopConfigTest.class})
public class AopTest extends BaseAppTest {

    @Resource
    private TargetInterfaceTest cglibProxiedTarget;

    @Test
    public void testProxiedByCGLIB() {


    }

}