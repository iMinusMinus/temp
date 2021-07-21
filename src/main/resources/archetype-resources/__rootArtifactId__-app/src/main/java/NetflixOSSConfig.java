package ${package};

#if($framework.contains('ribbon'))
import javax.annotation.PostConstruct;
#end
#if($framework.contains('eureka'))
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
#end
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
#if($framework.contains('eureka'))
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
#end
#if($framework.contains('ribbon'))
import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.client.ClientRequest;
import com.netflix.client.config.ClientConfigFactory;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.IClient;
import com.netflix.client.IResponse;
import com.netflix.client.RequestSpecificRetryHandler;
#end
#if($framework.contains('eureka') or $framework.contains('ribbon') or $framework.contains('hystrix') or $framework.contains('spectator'))
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext.ContextKey;
import com.netflix.config.DynamicPropertyFactory;
#end
#if($framework.contains('eureka'))
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.providers.CloudInstanceConfigProvider;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.converters.jackson.mixin.ApplicationsJsonMixIn;
import com.netflix.discovery.converters.jackson.mixin.InstanceInfoJsonMixIn;
import com.netflix.discovery.converters.jackson.serializer.InstanceInfoJsonBeanSerializer;
import com.netflix.discovery.providers.DefaultEurekaClientConfigProvider;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.netflix.discovery.util.StringUtil;
#end
#if($framework.contains('hystrix'))
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.Hystrix;
#end
#if($framework.contains('ribbon'))
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAffinityServerListFilter;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.NIWSDiscoveryPing;
#end
#if($framework.contains('spectator'))
import com.netflix.servo.publish.atlas.AtlasMetricObserver;
import com.netflix.servo.publish.atlas.BasicAtlasConfig;
import com.netflix.servo.publish.atlas.ServoAtlasConfig;
import com.netflix.servo.tag.TagList;
import com.netflix.spectator.api.Registry;
import com.netflix.spectator.servo.ServoRegistry;
#end
#if($framework.contains('feign'))
import feign.Client;
import feign.Client.Default;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.Contract;
import feign.Feign;
import feign.FeignException;
#if($framework.contains('hystrix'))
import feign.hystrix.HystrixFeign;
#end
import feign.jaxrs.JAXRSContract;
import feign.Logger;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Retryer;
#end

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
#if($framework.contains('eureka') or $framework.contains('feign'))
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
#end
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpMessageConverterExtractor;
#if($framework.contains('eureka'))
import org.springframework.web.client.RestTemplate;
#end

## // eureka-server和zuul应用已存在独立war，无需整合
/**
 * Netflix OSS configuration: eureka, feign, ribbon, hystrix, servo
 * @author iMinusMinus
 * @version 1.1.0
 */
@Configuration
@Import(value = RefreshConfiguration.class)
public class NetflixOSSConfig implements InitializingBean{
#if($framework.contains('eureka') or $framework.contains('ribbon') or $framework.contains('hystrix') or $framework.contains('spectator'))

    #[[@Value("${spring.profiles.active}")]]#
    private String activeProfiles;

    #[[@Value("${spring.application.name}")]]#
    private String appName;
#end

    public void afterPropertiesSet() throws Exception {
#if($framework.contains('eureka') or $framework.contains('ribbon') or $framework.contains('hystrix') or $framework.contains('spectator'))
        String placeholder = "${spring.profiles.active}";
        String env = ConfigurationManager.getDeploymentContext().getDeploymentEnvironment();
        if(env == null || env.length() == 0 || placeholder.equals(env)) {
            ConfigurationManager.getDeploymentContext().setDeploymentEnvironment(activeProfiles);
        }
        String stack = ConfigurationManager.getDeploymentContext().getDeploymentStack();
        if(stack != null && stack.contains(placeholder)) {
            ConfigurationManager.getDeploymentContext().setDeploymentStack(stack.replace(placeholder, ConfigurationManager.getDeploymentContext().getDeploymentEnvironment()));
        }
        String applicationId = ConfigurationManager.getDeploymentContext().getApplicationId();
        if(applicationId == null || applicationId.length() == 0) {
            ConfigurationManager.getDeploymentContext().setApplicationId(appName);
        }
#end
    }
#if($framework.contains('ribbon'))

    #[[@Value("${ribbon.eureka.approximateZoneFromHostname:false}")]]#
    private boolean approximateZoneFromHostname = false;

    #[[@Value("${ribbon.client.name:default}")]]#
    private String name = "client";

## // 启动命令加参数"-Dribbon.ClientClassName"也可生效
    static {
        System.setProperty("ribbon.ClientClassName", NetflixOSSConfig.LoadBalancerClient.class.getName()); // outter$inner, not outter.inner
    }
#end
#if($framework.contains('feign'))

    @Autowired(required = false)
    private List<HttpMessageConverter> converters;

    {
        if(converters == null) {
            converters = new ArrayList<HttpMessageConverter>();
        }
        if(converters.isEmpty()) {
            converters.add(new MappingJackson2HttpMessageConverter());
        }
    }

    @Autowired(required = false)
    private Logger logger;
#end
#if($framework.contains('spectator'))

    @Autowired(required = false)
    private TagList commonTags;
#end
#if($framework.contains('eureka'))
## refer: org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration

    static class RestTemplateEurekaHttpClient implements EurekaHttpClient {

        private RestTemplate restTemplate;

        private String serviceUrl;

        public RestTemplateEurekaHttpClient(RestTemplate restTemplate, String serviceUrl) {
            this.restTemplate = restTemplate;
            this.serviceUrl = serviceUrl;
            if (!serviceUrl.endsWith("/")) {
                this.serviceUrl = this.serviceUrl + "/";
            }
        }

        public String getServiceUrl() {
            return this.serviceUrl;
        }

        @Override
        public EurekaHttpResponse<Void> register(InstanceInfo info) {
            String urlPath = serviceUrl + "apps/" + info.getAppName();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.POST,
                    new HttpEntity<>(info, headers), Void.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue()).headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<Void> cancel(String appName, String id) {
            String urlPath = serviceUrl + "apps/" + appName + '/' + id;

            ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE,
                    null, Void.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue()).headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<InstanceInfo> sendHeartBeat(String appName, String id,
                                                              InstanceInfo info, InstanceInfo.InstanceStatus overriddenStatus) {
            String urlPath = serviceUrl + "apps/" + appName + '/' + id + "?status="
                    + info.getStatus().toString() + "&lastDirtyTimestamp="
                    + info.getLastDirtyTimestamp().toString() + (overriddenStatus != null
                    ? "&overriddenstatus=" + overriddenStatus.name() : "");

            ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath,
                    HttpMethod.PUT, null, InstanceInfo.class);

            EurekaHttpResponse.EurekaHttpResponseBuilder<InstanceInfo> eurekaResponseBuilder = EurekaHttpResponse.anEurekaHttpResponse(
                    response.getStatusCodeValue(), InstanceInfo.class)
                    .headers(headersOf(response));

            if (response.hasBody()) {
                eurekaResponseBuilder.entity(response.getBody());
            }

            return eurekaResponseBuilder.build();
        }

        @Override
        public EurekaHttpResponse<Void> statusUpdate(String appName, String id,
                                                     InstanceInfo.InstanceStatus newStatus, InstanceInfo info) {
            String urlPath = serviceUrl + "apps/" + appName + '/' + id + "/status?value="
                    + newStatus.name() + "&lastDirtyTimestamp="
                    + info.getLastDirtyTimestamp().toString();

            ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.PUT, null, Void.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue()).headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<Void> deleteStatusOverride(String appName, String id,
                                                             InstanceInfo info) {
            String urlPath = serviceUrl + "apps/" + appName + '/' + id
                    + "/status?lastDirtyTimestamp=" + info.getLastDirtyTimestamp().toString();

            ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE, null, Void.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue()).headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<Applications> getApplications(String... regions) {
            return getApplicationsInternal("apps/", regions);
        }

        private EurekaHttpResponse<Applications> getApplicationsInternal(String urlPath,
                                                                         String[] regions) {
            String url = serviceUrl + urlPath;

            if (regions != null && regions.length > 0) {
                url = url + (urlPath.contains("?") ? "&" : "?") + "regions=" + StringUtil.join(regions);
            }

            ResponseEntity<Applications> response = restTemplate.exchange(url,
                    HttpMethod.GET, null, Applications.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue(),
                    response.getStatusCode().value() == HttpStatus.OK.value()
                            && response.hasBody() ? response.getBody() : null)
                    .headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<Applications> getDelta(String... regions) {
            return getApplicationsInternal("apps/delta", regions);
        }

        @Override
        public EurekaHttpResponse<Applications> getVip(String vipAddress, String... regions) {
            return getApplicationsInternal("vips/" + vipAddress, regions);
        }

        @Override
        public EurekaHttpResponse<Applications> getSecureVip(String secureVipAddress, String... regions) {
            return getApplicationsInternal("svips/" + secureVipAddress, regions);
        }

        @Override
        public EurekaHttpResponse<Application> getApplication(String appName) {
            String urlPath = serviceUrl + "apps/" + appName;

            ResponseEntity<Application> response = restTemplate.exchange(urlPath,
                    HttpMethod.GET, null, Application.class);

            Application application = response.getStatusCodeValue() == HttpStatus.OK.value()
                    && response.hasBody() ? response.getBody() : null;

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue(), application)
                    .headers(headersOf(response)).build();
        }

        @Override
        public EurekaHttpResponse<InstanceInfo> getInstance(String appName, String id) {
            return getInstanceInternal("apps/" + appName + '/' + id);
        }

        @Override
        public EurekaHttpResponse<InstanceInfo> getInstance(String id) {
            return getInstanceInternal("instances/" + id);
        }

        private EurekaHttpResponse<InstanceInfo> getInstanceInternal(String urlPath) {
            urlPath = serviceUrl + urlPath;

            ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath,
                    HttpMethod.GET, null, InstanceInfo.class);

            return EurekaHttpResponse.anEurekaHttpResponse(response.getStatusCodeValue(),
                    response.getStatusCodeValue() == HttpStatus.OK.value()
                            && response.hasBody() ? response.getBody() : null)
                    .headers(headersOf(response)).build();
        }

        @Override
        public void shutdown() {
            // Nothing to do
        }

        private static Map<String, String> headersOf(ResponseEntity<?> response) {
            HttpHeaders httpHeaders = response.getHeaders();
            if (httpHeaders == null || httpHeaders.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<String, String> headers = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    headers.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            return headers;
        }

    }

    static class BasicAuthenticationInterceptor implements ClientHttpRequestInterceptor {
        private final String encodedCredentials;

        public BasicAuthenticationInterceptor(String username, String password) {
            this(username, password, null);
        }

        public BasicAuthenticationInterceptor(String username, String password, Charset charset) {
            if(charset == null) {
                charset = StandardCharsets.ISO_8859_1;
            }
            this.encodedCredentials = new String(Base64.getEncoder().encode((username + ":" + password).getBytes(charset)), charset);
        }

        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey("Authorization")) {
                headers.set("Authorization", "Basic " + this.encodedCredentials);
            }

            return execution.execute(request, body);
        }
    }

    static class RestTemplateTransportClientFactory implements TransportClientFactory {

        @Override
        public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
            return new RestTemplateEurekaHttpClient(restTemplate(serviceUrl.getServiceUrl()), serviceUrl.getServiceUrl());
        }

        private RestTemplate restTemplate(String serviceUrl) {
            RestTemplate restTemplate = new RestTemplate();
            try {
                URI serviceURI = new URI(serviceUrl);
                if (serviceURI.getUserInfo() != null) {
                    String[] credentials = serviceURI.getUserInfo().split(":");
                    if (credentials.length == 2) {
                        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(
                                credentials[0], credentials[1]));
                    }
                }
            } catch (URISyntaxException ignore) {

            }

            restTemplate.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
//            restTemplate.setErrorHandler(new ErrorHandler());

            return restTemplate;
        }

        private MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(new ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE));

            SimpleModule jsonModule = new SimpleModule();
            jsonModule.setSerializerModifier(createJsonSerializerModifier()); // keyFormatter,
            // compact));
            converter.getObjectMapper().registerModule(jsonModule);

            converter.getObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            converter.getObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE,
                    true);
            converter.getObjectMapper().addMixIn(Applications.class,
                    ApplicationsJsonMixIn.class);
            converter.getObjectMapper().addMixIn(InstanceInfo.class,
                    InstanceInfoJsonMixIn.class);

            // converter.getObjectMapper().addMixIn(DataCenterInfo.class,
            // DataCenterInfoXmlMixIn.class);
            // converter.getObjectMapper().addMixIn(InstanceInfo.PortWrapper.class,
            // PortWrapperXmlMixIn.class);
            // converter.getObjectMapper().addMixIn(Application.class,
            // ApplicationXmlMixIn.class);
            // converter.getObjectMapper().addMixIn(Applications.class,
            // ApplicationsXmlMixIn.class);

            return converter;
        }

        private static BeanSerializerModifier createJsonSerializerModifier() {
            return new BeanSerializerModifier() {
                @Override
                public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                                          BeanDescription beanDesc, JsonSerializer<?> serializer) {
                    if (beanDesc.getBeanClass().isAssignableFrom(InstanceInfo.class)) {
                        return new InstanceInfoJsonBeanSerializer(
                                (BeanSerializerBase) serializer, false);
                    }
                    return serializer;
                }
            };
        }

        @Override
        public void shutdown() {
        }

    }

## refer: org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration

    @Bean
    public AbstractDiscoveryClientOptionalArgs optionalArgs() {
        AbstractDiscoveryClientOptionalArgs optionalArgs = new AbstractDiscoveryClientOptionalArgs() {

        };
        optionalArgs.setTransportClientFactories(new TransportClientFactories<Void>() {

            @Override
            public TransportClientFactory newTransportClientFactory(
                    Collection<Void> additionalFilters, EurekaJerseyClient providedJerseyClient) {
                throw new UnsupportedOperationException();
            }

            @Override
            public TransportClientFactory newTransportClientFactory(
                    EurekaClientConfig clientConfig, Collection<Void> additionalFilters,
                    InstanceInfo myInstanceInfo) {
                return new RestTemplateTransportClientFactory();
            }

            @Override
            public TransportClientFactory newTransportClientFactory(
                    final EurekaClientConfig clientConfig,
                    final Collection<Void> additionalFilters, final InstanceInfo myInstanceInfo,
                    final Optional<SSLContext> sslContext,
                    final Optional<HostnameVerifier> hostnameVerifier) {
                return new RestTemplateTransportClientFactory();
            }
        });
        return optionalArgs;
    }

    @Bean
    public EurekaClientConfig eurekaClientConfigBean() {
        return new DefaultEurekaClientConfigProvider().get();
    }

    @Bean
    public EurekaInstanceConfig eurekaInstanceConfigBean() {
        return new CloudInstanceConfigProvider().get();
    }

    @Bean
    //@Lazy
    public ApplicationInfoManager eurekaApplicationInfoManager(EurekaInstanceConfig config) {
        return new ApplicationInfoManager(config, new EurekaConfigBasedInstanceInfoProvider(config).get());
    }

    @Bean(destroyMethod = "shutdown")
    @Scope("refresh")
    //@Lazy
    public DiscoveryClient eurekaClient(ApplicationInfoManager applicationInfoManager,
                                        EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs optionalArgs) {
        return new DiscoveryClient(applicationInfoManager, config, optionalArgs);
    }
#end
#if($framework.contains('feign'))

## // @EnableFeignClients, @FeignClient默认配置类为空，即注册的FeignClientSpecification的configuration为空数组
## // 虽然不存在默认的FeignClientSpecification，但FeignContext的默认配置类有FeignClientsConfiguration
## refer: org.springframework.cloud.netflix.feign.FeignContext
## refer: org.springframework.cloud.netflix.feign.FeignClientsConfiguration
## refer: org.springframework.cloud.netflix.feign.FeignAutoConfiguration
#if($framework.contains('ribbon'))
## refer: org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration
## refer: org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient
## refer: org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory
## refer: org.springframework.cloud.netflix.feign.ribbon.FeignLoadBalancer

    static class RibbonRequest extends ClientRequest implements Cloneable { // refer: org.springframework.cloud.netflix.feign.ribbon.FeignLoadBalancer.RibbonRequest

        private final Request request;

        private final Client client;

        RibbonRequest(Client client, Request request, URI uri) {
            this.client = client;
            setUri(uri);
            this.request = toRequest(request);
        }

        private Request toRequest(Request request) {
            Map<String, Collection<String>> headers = new LinkedHashMap<>(request.headers());
            return Request.create(request.method(), getUri().toASCIIString(), headers, request.body(), request.charset());
        }

        Request toRequest() {
            return toRequest(this.request);
        }

        Client client() {
            return this.client;
        }

        HttpRequest toHttpRequest() {
            return new HttpRequest() {
                @Override
                public HttpMethod getMethod() {
                    return HttpMethod.resolve(RibbonRequest.this.toRequest().method());
                }

                @Override
                public URI getURI() {
                    return RibbonRequest.this.getUri();
                }

                @Override
                public HttpHeaders getHeaders() {
                    Map<String, List<String>> headers = new HashMap<String, List<String>>();
                    Map<String, Collection<String>> feignHeaders = RibbonRequest.this.toRequest().headers();
                    for (String key : feignHeaders.keySet()) {
                        headers.put(key, new ArrayList<String>(feignHeaders.get(key)));
                    }
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(headers);
                    return httpHeaders;

                }
            };
        }


        @Override
        public Object clone() {
            return new RibbonRequest(this.client, this.request, getUri());
        }
    }

    static class RibbonResponse implements IResponse { // refer: org.springframework.cloud.netflix.feign.ribbon.FeignLoadBalancer.RibbonResponse

        private final URI uri;
        private final Response response;

        RibbonResponse(URI uri, Response response) {
            this.uri = uri;
            this.response = response;
        }

        @Override
        public Object getPayload() throws ClientException {
            return this.response.body();
        }

        @Override
        public boolean hasPayload() {
            return this.response.body() != null;
        }

        @Override
        public boolean isSuccess() {
            return this.response.status() == 200;
        }

        @Override
        public URI getRequestedURI() {
            return this.uri;
        }

        @Override
        public Map<String, Collection<String>> getHeaders() {
            return this.response.headers();
        }

        Response toResponse() {
            return this.response;
        }

        @Override
        public void close() throws IOException {
            if (this.response != null && this.response.body() != null) {
                this.response.body().close();
            }
        }
    }

## // refer: org.springframework.cloud.netflix.feign.ribbon.FeignLoadBalancer
## // refer: com.netflix.niws.client.http.RestClient
    public static class LoadBalancerClient extends AbstractLoadBalancerAwareClient<RibbonRequest, RibbonResponse> {

        public LoadBalancerClient() {
            super(null);
        }

        public LoadBalancerClient(IClientConfig clientConfig) {
            super(null, clientConfig);
            initWithNiwsConfig(clientConfig);
        }

        public LoadBalancerClient(ILoadBalancer lb, IClientConfig clientConfig) {
            super(lb, clientConfig);
            initWithNiwsConfig(clientConfig);
        }

        @Override
        public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride) throws IOException {
            int defaultConnectTimeoutMillis = 2000; // DefaultClientConfigImpl.DEFAULT_CONNECT_TIMEOUT
            int defaultReadTimeoutMillis = 5000; // DefaultClientConfigImpl.DEFAULT_READ_TIMEOUT
            int connectTimeoutMillis = (configOverride != null) ? configOverride.get(CommonClientConfigKey.ConnectTimeout, defaultConnectTimeoutMillis) : defaultConnectTimeoutMillis;
            int readTimeoutMillis = (configOverride != null) ? configOverride.get(CommonClientConfigKey.ReadTimeout, defaultReadTimeoutMillis) : defaultReadTimeoutMillis;
            Request.Options options = new Request.Options(connectTimeoutMillis, readTimeoutMillis);
            Response response = request.client().execute(request.toRequest(), options);
            return new RibbonResponse(request.getUri(), response);
        }

        @Override
        public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonRequest request, IClientConfig requestConfig) {
            return new RequestSpecificRetryHandler(requestConfig.get(CommonClientConfigKey.RequestSpecificRetryOn, true),
                    requestConfig.get(CommonClientConfigKey.OkToRetryOnAllOperations, false));
        }
    }

## // refer: org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient
    public static class LoadBalancerFeignClient implements Client {

        private final Client delegate; // httpclient, okhttp, etc.

        private final String name;

        public LoadBalancerFeignClient(String serviceId, Client delegate) {
            this.name = serviceId;
            this.delegate = delegate;
        }

        @Override
        public Response execute(Request request, Request.Options options) throws IOException {
            IClientConfig config = ClientFactory.getNamedConfig(name);
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            RibbonRequest ribbonRequest = new RibbonRequest(delegate, request, URI.create(request.url().replaceFirst(clientName, "")));
            AbstractLoadBalancerAwareClient<RibbonRequest, RibbonResponse> ribbonClient = (AbstractLoadBalancerAwareClient) ClientFactory.getNamedClient(name);
            try {
                return ribbonClient.executeWithLoadBalancer(ribbonRequest, config).toResponse(); // executeWithLoadBalancer方法可以从request获取loadBalancerKey，ILoadBalancer/IRule在选取实例时可根据loadBalancerKey筛选
            } catch (ClientException e) {
                IOException io = null;
                Throwable t = e;
                while (true) {
                    if (t == null) {
                        break;
                    }
                    if (t instanceof IOException) {
                        io = (IOException) t;
                    }
                    t = t.getCause();
                }
                if (io != null) {
                    throw io;
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Bean
    public Client feignClient() {
        return new LoadBalancerFeignClient(name, new Client.Default(null, null));
    }
#else

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
#end

    private static class FeignOutputMessage implements HttpOutputMessage {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private final HttpHeaders httpHeaders;

        private FeignOutputMessage(RequestTemplate request) {
            httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : request.headers().entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
        }

        @Override
        public OutputStream getBody() throws IOException {
            return this.outputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.httpHeaders;
        }

        public ByteArrayOutputStream getOutputStream() {
            return this.outputStream;
        }

    }

    static class SpringEncoder implements Encoder {
        private final List<HttpMessageConverter> converters;

        public SpringEncoder(List<HttpMessageConverter> converters) {
            this.converters = converters;
        }

        @Override
        public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
            if(requestBody == null) {
                return;
            }
            Class<?> requestType = requestBody.getClass();
            Collection<String> contentTypes = request.headers().get("Content-Type");

            MediaType requestContentType = null;
            if (contentTypes != null && !contentTypes.isEmpty()) {
                String type = contentTypes.iterator().next();
                requestContentType = MediaType.valueOf(type);
            }
            for(HttpMessageConverter messageConverter : converters) {
                if (messageConverter.canWrite(requestType, requestContentType)) {
                    FeignOutputMessage outputMessage = new FeignOutputMessage(request);
                    try {
                        @SuppressWarnings("unchecked")
                        HttpMessageConverter<Object> copy = (HttpMessageConverter<Object>) messageConverter;
                        copy.write(requestBody, requestContentType, outputMessage);
                    }
                    catch (IOException ex) {
                        throw new EncodeException("Error converting request body", ex);
                    }
                    request.headers(null);
                    LinkedHashMap<String, Collection<String>> headers = new LinkedHashMap<>();
                    for (Map.Entry<String, List<String>> entry : outputMessage.getHeaders().entrySet()) {
                        headers.put(entry.getKey(), entry.getValue());
                    }
                    request.headers(headers);
                    if (messageConverter instanceof ByteArrayHttpMessageConverter) {
                        request.body(outputMessage.getOutputStream().toByteArray(), null);
                    } else {
                        request.body(outputMessage.getOutputStream().toByteArray(), Charset.forName("UTF-8"));
                    }
                    return;
                }
            }
            String message = "Could not write request: no suitable HttpMessageConverter "
                    + "found for request type [" + requestType.getName() + "]";
            if (requestContentType != null) {
                message += " and content type [" + requestContentType + "]";
            }
            throw new EncodeException(message);
        }
    }

    /**
     * @see org.springframework.cloud.netflix.feign.support.SpringEncoder
     */
    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(converters);
    }

    private static class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(this.response.status());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.status();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.reason();
        }

        @Override
        public void close() {
            try {
                this.response.body().close();
            }
            catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : this.response.headers().entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return httpHeaders;
        }

    }

    static class SpringDecoder implements Decoder {
        private final List<HttpMessageConverter> converters;

        public SpringDecoder(List<HttpMessageConverter> converters) {
            this.converters = converters;
        }

        @Override
        public Object decode(final Response response, Type type) throws IOException, FeignException {
            if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(type, converters);
                return extractor.extractData(new FeignResponseAdapter(response));
            }
            throw new DecodeException("type is not an instance of Class or ParameterizedType: " + type);
        }
    }

    /**
     * @see : org.springframework.cloud.netflix.feign.support.SpringDecoder
     * @return Decoder
     */
    @Bean
    public Decoder feignDecoder() {
        return new SpringDecoder(converters);
    }


    @Bean
    public Contract feignContract() {
        return new JAXRSContract();
    }
#** feign.Feign.Builder
    @Bean
    public Decoder feignDecoder() {
        return new feign.codec.Decoder.Default();
    }

    @Bean
    public Encoder feignEncoder() {
        return  new feign.codec.Encoder.Default();
    }

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

#if($framework.contains('hystrix'))
    @Bean
    @Scope("prototype")
    public Feign.Builder feignHystrixBuilder() {
        return HystrixFeign.builder();
    }
#else
    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(Retryer retryer) {
        return Feign.builder().retryer(retryer);
    }
#end
*#
#end
#if($framework.contains('ribbon'))

## // SpringClientFactory默认使用RibbonClientConfiguration作为配置类
## refer: org.springframework.cloud.netflix.ribbon.SpringClientFactory
## refer: org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration
## // 当eureka存在时，RibbonEurekaAutoConfiguration通过@RibbonClients引入了EurekaRibbonClientConfiguration作为SpringClientFactory的configurations
## refer: org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration
## refer: org.springframework.cloud.netflix.ribbon.RibbonClients
## refer: org.springframework.cloud.netflix.ribbon.RibbonClientConfigurationRegistrar
## refer: org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration
## // 不同的clientName可以通过@RibbonClient设置不同的配置类，注册到spring成为RibbonClientSpecification，被设置为SpringClientFactory的configurations
## // 当通过clientName获取IClient等对象时，SpringClientFactory创建AnnotationConfigApplicationContext，将"ribbon.client.name"作为key,clientName作为value，放到上下文的环境属性配置
## refer: org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration
#** com.netflix.client.ClientFactory
    @Bean
    public IClientConfig ribbonClientConfig() {
        IClientConfig config = ClientConfigFactory.DEFAULT.newConfig(); // new DefaultClientConfigImpl()
        config.loadProperties(this.name);
        return config;
    }

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        String ruleClass = DynamicPropertyFactory.getInstance()
                .getStringProperty(name + ".ribbon.NFLoadBalancerRuleClassName", null).get();
        if(ruleClass != null) { // config.get(CommonClientConfigKey.NFLoadBalancerRuleClassName)
            return instantiateWithConfig(IRule.class, ruleClass, config);
        }
        ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
        rule.initWithNiwsConfig(config);
        return rule;
    }

    @Bean
    public ServerListUpdater ribbonServerListUpdater(IClientConfig config) {
        return new PollingServerListUpdater(config); // config.get(CommonClientConfigKey.ServerListUpdaterClassName)
    }

    @Bean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
                                            ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
                                            IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        String lbClass = DynamicPropertyFactory.getInstance()
                .getStringProperty(name + ".ribbon.NFLoadBalancerClassName", null).get();
        if(lbClass != null) { // config.get(CommonClientConfigKey.NFLoadBalancerClassName)
            return instantiateWithConfig(ILoadBalancer.class, lbClass, config);
        }
        return new ZoneAwareLoadBalancer<>(config, rule, ping, serverList, serverListFilter, serverListUpdater);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public ServerListFilter<Server> ribbonServerListFilter(IClientConfig config) {
        String serverListFilterClass = DynamicPropertyFactory.getInstance()
                .getStringProperty(name + ".ribbon.NIWSServerListFilterClassName", null).get();
        if(serverListFilterClass != null) { // config.get(CommonClientConfigKey.NIWSServerListFilterClassName)
            return instantiateWithConfig(ServerListFilter.class, serverListFilterClass, config);
        }
        ZoneAffinityServerListFilter filter = new ZoneAffinityServerListFilter();
        filter.initWithNiwsConfig(config);
        return filter;
    }

    @PostConstruct
    public void preprocess() {
        String VALUE_NOT_SET = "__not__set__";
        String key = name + ".ribbon." + CommonClientConfigKey.DeploymentContextBasedVipAddresses.key();
        String vipAddress = DynamicPropertyFactory.getInstance().getStringProperty(key, VALUE_NOT_SET).get();
        if(VALUE_NOT_SET.equals(vipAddress)) {
            // 使得DiscoveryEnabledNIWSServerList获取的vipAddress不为空，getUpdatedListOfServers时，从eureka发现注册的实例
            ConfigurationManager.getConfigInstance().setProperty(key, name);
        }

        ConfigurationManager.getConfigInstance().setProperty(name + ".ribbon." + CommonClientConfigKey.EnableZoneAffinity.key(), "true");

        String zone = ConfigurationManager.getDeploymentContext().getValue(ContextKey.zone);
        if(zone != null && !zone.isEmpty()) {
            return;
        }
        EurekaInstanceConfig eurekaConfig = eurekaInstanceConfigBean();
        zone = eurekaConfig.getMetadataMap().get("zone");
        if(zone != null && !zone.isEmpty()) {
            ConfigurationManager.getDeploymentContext().setValue(ContextKey.zone, zone);
            return;
        }
        EurekaClientConfig clientConfig = eurekaClientConfigBean();
        String[] availabilityZone = clientConfig.getAvailabilityZones(clientConfig.getRegion());
        zone = (availabilityZone != null && availabilityZone.length > 0) ? availabilityZone[0] : null;
        if(zone != null && !zone.isEmpty()) {
            ConfigurationManager.getDeploymentContext().setValue(ContextKey.zone, zone);
            return;
        }
    }

## refer: org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration

    @Bean
    public IPing ribbonPing(IClientConfig config) {
        String pingClass = DynamicPropertyFactory.getInstance()
                .getStringProperty(name + ".ribbon.NFLoadBalancerPingClassName", null).get();
        if(pingClass != null) { // config.get(CommonClientConfigKey.NFLoadBalancerPingClassName)
            return instantiateWithConfig(IPing.class, pingClass, config);
        }
        NIWSDiscoveryPing ping = new NIWSDiscoveryPing(); // eureka present or use DummyPing!
        ping.initWithNiwsConfig(config);
        return ping;
    }

    // initialize after eureka!
    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> eurekaClientProvider) {
        String serverListClass = DynamicPropertyFactory.getInstance()
                .getStringProperty(name + ".ribbon.NIWSServerListClassName", null).get();
        if(serverListClass != null) { // config.get(CommonClientConfigKey.NIWSServerListClassName)
           return instantiateWithConfig(ServerList.class, serverListClass, config);
        }
        DiscoveryEnabledNIWSServerList discoveryServerList = new DiscoveryEnabledNIWSServerList(
                config, eurekaClientProvider); // eureka present or use ConfigurationBasedServerList!
        // spring-cloud-netflix提供了DomainExtractingServerList，从metadata中获取zone和instanceId
        return discoveryServerList;
    }

    private <C> C instantiateWithConfig(Class<C> type, String className, IClientConfig config) {
        if (className == null || className.trim().length() <= 0) {
            return null;
        }
        C result = null;
        try {
            result = (C) ClientFactory.instantiateInstanceWithClientConfig(className, config);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown class to load " + className + " for class " + type + " named " + name);
        } catch (Throwable e) {
            // Ignored
        }
        return result;
    }
*#
#end
#if($framework.contains('hystrix'))

## refer: org.springframework.cloud.netflix.hystrix.HystrixCircuitBreakerConfiguration
    @Bean
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }

    @Bean
    public HystrixShutdownHook hystrixShutdownHook() {
        return new HystrixShutdownHook();
    }

    private class HystrixShutdownHook implements DisposableBean {

        @Override
        public void destroy() throws Exception {
            // Just call Hystrix to reset thread pool etc.
            Hystrix.reset();
        }

    }
#end
#if($framework.contains('spectator'))
## refer: org.springframework.cloud.netflix.metrics.atlas.AtlasConfiguration
## refer: org.springframework.boot.actuate.autoconfigure.MetricExportAutoConfiguration
    // spring-cloud-netflix将 AtlasMetricObserver包装成Exporter，使用MetricExporters遍历Exporter定时推送
    @Bean
    public AtlasMetricObserver atlasObserver() {
        ServoAtlasConfig config = new BasicAtlasConfig() {
            @Override
            public String getAtlasUri() {
                return DynamicPropertyFactory.getInstance().getStringProperty("servo.atlas.uri", null).get();
            }

            @Override
            public int getPushQueueSize() {
                return DynamicPropertyFactory.getInstance().getIntProperty("servo.atlas.queueSize", 1000).get();
            }

            @Override
            public boolean shouldSendMetrics() {
                return DynamicPropertyFactory.getInstance().getBooleanProperty("servo.atlas.enabled", true).get();
            }

            @Override
            public int batchSize() {
                return DynamicPropertyFactory.getInstance().getIntProperty("servo.atlas.batchSize", 10000).get();
            }
        };
        return new AtlasMetricObserver(config, commonTags); // spring-cloud-netflix使用restTemplate推送到atlas
    }
## refer: org.springframework.cloud.netflix.metrics.servo.ServoMetricsAutoConfiguration
## refer: org.springframework.cloud.netflix.metrics.spectator.SpectatorMetricsAutoConfiguration
    @Bean
    public Registry registry() { // autowire Registry and use it create metric and register!
        return new ServoRegistry();
    }
#end
}