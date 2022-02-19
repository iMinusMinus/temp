package ${package};

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;

import javax.validation.Validator;

#if(!$configType.contains('xml'))
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
#end
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
#if(!$configType.contains('xml'))
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
#if($framework.contains('thymeleaf'))
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
#end
#end

/**
 * Web mvc configuration
 *
 * @see org.springframework.web.servlet.config.annotation.EnableWebMvc
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
#if($showComment)
## // Configuration annotation marker this class as Configuration class, then parse ComponentScan annotation and handle BeanMethod.
## // EnableWebMvc annotation import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration.
## // spring container inject this WebMvcConfigurer to configurers of DelegatingWebMvcConfiguration.
#end
#if(!$configType.contains('xml'))
@Configuration
@ComponentScan(value = "${package}", excludeFilters = @ComponentScan.Filter(value = {Service.class, Repository.class, Configuration.class}))
@EnableWebMvc
#end
public class MvcConfig extends WebMvcConfigurerAdapter {

#if(!$configType.contains('xml'))

    @Value("${spring.messages.basename}")
    private String basenames;

    @Value("${spring.messages.encoding}")
    private String encoding;

    @Value("${spring.messages.fallbackToSystemLocale}")
    private boolean fallbackToSystemLocale;

    @Value("${spring.messages.alwaysUseMessageFormat}")
    private boolean alwaysUseMessageFormat;

    @Value("${spring.messages.useCodeAsDefaultMessage}")
    private boolean useCodeAsDefaultMessage;

    @Value("${spring.mvc.view.prefix}")
    private String prefix;

    @Value("${spring.mvc.view.suffix}")
    private String suffix;

    @Value("${spring.mvc.localeResolver}")
    private String localeResolver;

    @Value("${spring.mvc.local}")
    private String local;
#if($framework.contains('thymeleaf'))

    @Value("${spring.thymeleaf.mode}")
    private String mode;

    @Value("${spring.thymeleaf.cache}")
    private boolean cache;

    @Value("${spring.thymeleaf.templateResolverOrder}")
    private Integer templateResolverOrder;

    @Value("${spring.thymeleaf.checkTemplate}")
    private boolean checkTemplate;

    @Value("${spring.thymeleaf.enableSpringElCompiler}")
    private boolean enableSpringElCompiler;

    @Value("${spring.thymeleaf.servlet.contentType}")
    private String contentType;

    @Value("${spring.thymeleaf.viewNames}")
    private String[] viewNames;

    @Value("${spring.thymeleaf.excludedViewNames}")
    private String[] excludedViewNames;
#end

    /**
     * Add cors config for global settings. (Refer: org.springframework.web.servlet.handler.AbstractHandlerMapping)
     * Another way by using @CrossOrigin on HandlerMethod/Controller. (Refer: org.springframework.web.servlet.handler.AbstractHandlerMethodMapping)
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
#if($showComment)
//        registry.addMapping("/**")
//                .allowedOrigins(CorsConfiguration.ALL)
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
//                .allowedHeaders(CorsConfiguration.ALL)
//                .allowCredentials(false)
//                .maxAge(1800L);
#end
    }

## // refer: org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(basenames));
        if (encoding != null) {
            messageSource.setDefaultEncoding(encoding);
        }
        messageSource.setFallbackToSystemLocale(fallbackToSystemLocale);
        messageSource.setAlwaysUseMessageFormat(alwaysUseMessageFormat);
        messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
        return messageSource;
    }
#if($showDefault)
## // AnnotationConfigWebApplicationContext.loadBeanDefinitions-->ClassPathBeanDefinitionScanner.doScan，bean不存在则注册
## // org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors

    /**
     * BeanFactoryPostProcessor processing classes with spring's Configuration annotation.
     *
     * @see org.springframework.context.annotation.Configuration
     * @return ConfigurationClassPostProcessor
     */
    @Bean(name = AnnotationConfigUtils.CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public ConfigurationClassPostProcessor internalConfigurationAnnotationProcessor() {
        return new ConfigurationClassPostProcessor();
    }

    /**
     * BeanPostProcessor that autowires annotated fields, setter methods and arbitrary config methods.
     *
     * @see org.springframework.beans.factory.annotation.Autowired
     * @see org.springframework.beans.factory.annotation.Value
     * @see javax.inject.Inject
     * @return AutowiredAnnotationBeanPostProcessor
     */
    @Bean(name = AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public AutowiredAnnotationBeanPostProcessor internalAutowiredAnnotationProcessor() {
        return new AutowiredAnnotationBeanPostProcessor();
    }

    /**
     * BeanPostProcessor check required properties.
     *
     * @see org.springframework.beans.factory.annotation.Required
     * @return RequiredAnnotationBeanPostProcessor
     */
    @Bean(name = AnnotationConfigUtils.REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public RequiredAnnotationBeanPostProcessor internalRequiredAnnotationProcessor() {
        return new RequiredAnnotationBeanPostProcessor();
    }

    /**
     * BeanPostProcessor that supports common Java annotations.
     *
     * @see javax.annotation.PostConstruct
     * @see javax.annotation.PreDestroy
     * @see javax.annotation.Resource
     * @see javax.xml.ws.WebServiceRef
     * @see javax.ejb.EJB
     * @return CommonAnnotationBeanPostProcessor
     */
    @Bean(name = AnnotationConfigUtils.COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    public CommonAnnotationBeanPostProcessor internalCommonAnnotationProcessor() {
        return new CommonAnnotationBeanPostProcessor();
    }

    //  define PersistenceAnnotationBeanPostProcessor if jpa present

#end

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /**
     * fixed or from accept header, cookie, session,
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        if ("FIXED".equals(localeResolver)) {
            return new FixedLocaleResolver(Locale.getDefault());
        }
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.getDefault());
        return localeResolver;
    }

    @Bean
    public ThemeResolver themeResolver() {
        return new CookieThemeResolver(); // fixed or session
    }
#if($showDefault)

    // HandleMapping, HandlerAdapter, HandlerExceptionResolver, FormattingConversionService, ContentNegotiationManager, Validator was defined in super class

    @Bean
    public RequestToViewNameTranslator viewNameTranslator() {
        return new DefaultRequestToViewNameTranslator();
    }

    @Bean
    public FlashMapManager flashMapManager() {
        return new SessionFlashMapManager();
    }
#end

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        return resolver;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return resolver;
    }

    @Bean
    public ContentNegotiatingViewResolver viewResolver(ContentNegotiationManager contentNegotiationManager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(contentNegotiationManager);
        // ContentNegotiatingViewResolver uses all the other view resolvers to locate
        // a view so it should have a high precedence
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }

## // refer: org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(Environment environment, @Lazy Validator validator) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        boolean proxyTargetClass = environment.getProperty("spring.aop.proxyTargetClass", Boolean.class, true);
        processor.setProxyTargetClass(proxyTargetClass);
        processor.setValidator(validator);
        return processor;
    }
#if($framework.contains('thymeleaf'))

## // refer: org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
    @Bean
    public SpringResourceTemplateResolver defaultTemplateResolver(ApplicationContext applicationContext, Environment environment) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(environment.getProperty("spring.thymeleaf.prefix"));
        resolver.setSuffix(environment.getProperty("spring.thymeleaf.suffix"));
        resolver.setTemplateMode(mode);
        resolver.setCharacterEncoding(environment.getProperty("spring.thymeleaf.encoding"));
        resolver.setCacheable(cache);
        if (templateResolverOrder != null) {
            resolver.setOrder(templateResolverOrder);
        }
        resolver.setCheckExistence(checkTemplate);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(Collection<ITemplateResolver> templateResolvers, Collection<IDialect> dialects) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(enableSpringElCompiler);
        templateResolvers.forEach(engine::addTemplateResolver);
        dialects.forEach(engine::addDialect); // spring-security dialect, java8 dialect
        return engine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine, Environment environment) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding(environment.getProperty("spring.thymeleaf.encoding"));
        MimeType type = MimeType.valueOf(contentType);
        if(type.getCharset() == null) {
            LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
            parameters.put("charset", environment.getProperty("spring.thymeleaf.encoding"));
            parameters.putAll(type.getParameters());
            type = new MimeType(type, parameters);
        }
        resolver.setContentType(type.toString());
        resolver.setExcludedViewNames(excludedViewNames);
        resolver.setViewNames(viewNames);
        // This resolver acts as a fallback resolver (e.g. like a
        // InternalResourceViewResolver) so it needs to have low precedence
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        resolver.setCache(cache);
        return resolver;
    }
#end
#end

}