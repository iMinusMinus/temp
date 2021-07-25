package ${package}.aop;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AopConfigTest.class})
public class AopTest {

    @Resource
    private TargetInterfaceTest cglibProxiedTarget;

    @Test
    public void testProxiedByCGLIB() {
        Assert.assertTrue(cglibProxiedTarget.getClass().getCanonicalName().contains("CGLIB"));
    }

}