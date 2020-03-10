package ${package}.web;

import ${package}.api.GenericService;
import ${package}.BaseMvcTest;

import org.springframework.test.context.ContextConfiguration;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath:caucho.xml"})
public class CauchoRemotingTest extends BaseMvcTest {

    @Resource
    private com.caucho.hessian.test.Test hessianClient;

    @Resource
    private GenericService burlapClient;

    @Test
    @Ignore//TODO The service has no method named: echo
    public void testHessian() {
        Object request = "<any>";
        Assert.assertEquals(request, hessianClient.echo(request));
    }

    @Test
    @Ignore//TODO url path
    public void testBurlap() throws Exception {
        String[] parameterTypes = new String[0];
        Object[] args = new String[0];
        System.out.println(burlapClient.$invoke("javaMethod", parameterTypes, args));
    }

}