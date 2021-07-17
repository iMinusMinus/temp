package ${package};

import ${package}.api.EchoService;
import ${package}.api.GenericService;
import ${package}.api.in.EchoRequest;
import ${package}.api.out.EchoResponse;

#if($framework.contains('feign'))
import feign.auth.BasicAuthRequestInterceptor;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.Contract;
#if($framework.contains('hystrix'))
import feign.hystrix.HystrixFeign;
#else
import feign.Feign;
#end
import feign.Logger.Level;
import feign.Response;
import feign.Retryer;;
import feign.slf4j.Slf4jLogger;
#end
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
#if($framework.contains('feign'))

    @Resource
    private Encoder feignEncoder;

    @Resource
    private Decoder feignDecoder;

    @Resource
    private Client feignClient;

    @Resource
    private Contract feignContract;
#end

    @Test
    public void testInvoke() throws Exception {
        genericService.$invoke("echo", new String[]{"${package}.api.in.EchoRequest"}, new Object[]{null});
    }

#if($framework.contains('feign'))
    @Test
    public void testEcho() throws Exception {
#if($framework.contains('eureka'))
        String name = "stats";
        String url = "http://" + name; // TODO: http must?
#else
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/";
#end
        EchoService service = #if($framework.contains('hystrix'))Hystrix#{end}Feign.builder()
            .client(feignClient)
            .contract(feignContract)
            .encoder(feignEncoder)
            .decoder(feignDecoder)
            .logger(new Slf4jLogger()).logLevel(Level.FULL)
            .requestInterceptor(new BasicAuthRequestInterceptor("username", "password")).retryer(Retryer.NEVER_RETRY)
            .target(EchoService.class, url#{if}($framework.contains('hystrix')), new EchoService() {
                @Override
                public EchoResponse echo(EchoRequest request) {
                    return new EchoResponse();
                }
            }#{end});
        System.out.println(service.echo(new EchoRequest()));
    }
#end

}