package ${package}.aop;

import org.junit.Test;

import ${package}.BaseAppTest;
import ${package}.AppConfig;

@ContextConfiguration(classes = {AppConfig.class, AopConfigTest.class})
public class AopTest extends BaseAppTest {

    @Resource
    private TargetInterfaceTest cglibProxiedTarget;

    @Test
    public void testProxiedByCGLIB() {


    }

}