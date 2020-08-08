package ${package};

import ${package}.api.GenericService;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * RPC consumer test
 *
 * @author iMinusMinus
 * @date 2020-03-21
 */
public class RpcTest extends ContainerBase {

    @Resource
    private GenericService genericService;

    @Test
    public void testInvoke() throws Exception {
        genericService.$invoke("echo", new String[]{"${package}.api.in.EchoRequest"}, new Object[]{null});
    }

}