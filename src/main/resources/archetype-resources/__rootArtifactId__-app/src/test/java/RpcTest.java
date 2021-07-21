package ${package};

import ${package}.api.GenericService;
import ${package}.rs.BankCardValidationService;
import ${package}.rs.dto.BankCardValidationResult;

#if($framework.contains('eureka') or $framework.contains('ribbon') or $framework.contains('hystrix') or $framework.contains('spectator'))
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
#end
#if($framework.contains('eureka'))
import com.netflix.discovery.EurekaClient;
#end
#if($framework.contains('feign'))
import feign.auth.BasicAuthRequestInterceptor;
import feign.Client;
import feign.Client.Default;
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

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * RPC consumer test
 *
 * @author iMinusMinus
 * @date 2020-03-21
 */
public class RpcTest extends ContainerBase {
#if($framework.contains('eureka') or $framework.contains('ribbon') or $framework.contains('hystrix') or $framework.contains('spectator'))

    #[[@Value("${spring.profiles.active}")]]#
    private String activeProfile;

    @Test
    public void testArchaius() {
        Assert.assertEquals(activeProfile, ConfigurationManager.getDeploymentContext().getDeploymentEnvironment());
    }
#end

    @Resource
    private GenericService genericService;

#if($framework.contains('eureka'))

    @Resource
    private EurekaClient eurekaClient;

    @Test
    public void testEureka() {
        Assert.assertNotNull(eurekaClient.getApplications());
    }
#end
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
    public void testFeign() throws Exception {
        String name = "alipay";
        String url = "https://" + name; // scheme: http optional, https must!
#if($framework.contains('ribbon'))
        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.EnableZoneAffinity.key(), "true");
        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.DeploymentContextBasedVipAddresses.key(), name);
#if($framework.contains('eureka'))
        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.NIWSServerListClassName.key(),
                "com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList");
#else
        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.NIWSServerListClassName.key(),
                "com.netflix.loadbalancer.ConfigurationBasedServerList");
        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.ListOfServers.key(),
                "ccdcapi.alipay.com:443"); // 当协议为https时，即便是默认端口，也必须带上，否则使用http默认的80端口
#end
#end
        BankCardValidationService service = #if($framework.contains('hystrix'))Hystrix#{end}Feign.builder()
            .client(new NetflixOSSConfig.LoadBalancerFeignClient(name, new Client.Default(new sun.security.ssl.SSLSocketFactoryImpl(), (hostname, session) -> true)))
            .contract(feignContract)
            .encoder(feignEncoder)
            .decoder(feignDecoder)
            .logger(new Slf4jLogger()).logLevel(Level.FULL)
            .requestInterceptor(new BasicAuthRequestInterceptor("username", "password")) // just for fun
            .retryer(Retryer.NEVER_RETRY)
            .target(BankCardValidationService.class, url#{if}($framework.contains('hystrix')), new BankCardValidationService() {
                @Override
                public BankCardValidationResult validate(String cardNo, boolean cardBinCheck) {
                    BankCardValidationResult mock = new BankCardValidationResult();
                    mock.setKey(cardNo);
                    return mock;
                }
            }#{end});
        String cardNo = "6227003320232234322";
        BankCardValidationResult response = service.validate(cardNo, true);
        Assert.assertEquals(cardNo, response.getKey());
    }
#end

}