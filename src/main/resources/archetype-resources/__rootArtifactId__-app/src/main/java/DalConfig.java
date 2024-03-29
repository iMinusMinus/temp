package ${package};

import javax.sql.DataSource;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;
#if($framework.contains('jta'))
#if($container.contains('weblogic'))
import org.springframework.transaction.jta.WebLogicJtaTransactionManager;
#elseif($container.contains('websphere'))
import org.springframework.transaction.jta.WebSphereUowTransactionManager;
#elseif($container.contains('oc4j'))
import org.springframework.transaction.jta.OC4JJtaTransactionManager;
#else
import org.springframework.transaction.jta.JtaTransactionManager;
#end
#else
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
#end
#if($framework.contains('mybatis'))
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
#end
#if($framework.contains('hibernate'))
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.hibernate.SessionFactory;
#end

#if($framework.contains('druid'))
import com.alibaba.druid.pool.DruidDataSource;
#end
#if($framework.contains('hikari'))
import com.zaxxer.hikari.HikariDataSource;
#end

@Configuration
#if($framework.contains('mybatis'))
@MapperScan(basePackages = {"${package}.dao"})
#end
public class DalConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

#if($framework.contains("druid"))
    @Value("${spring.datasource.druid.dbType}")
    private String dbType;
#end

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;

//    @Value("${mybatis.mapperLocations}")
//    private Resource[] mapperLocations;

#if($framework.contains('jta'))
    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
#if($container.contains('weblogic'))
        PlatformTransactionManager transactionManager = new WebLogicJtaTransactionManager();
#elseif($container.contains('websphere'))
        PlatformTransactionManager transactionManager = new WebSphereUowTransactionManager();
#elseif($container.contains('oc4j'))
        PlatformTransactionManager transactionManager = new OC4JJtaTransactionManager();
#else
        PlatformTransactionManager transactionManager = new JtaTransactionManager();
#end
        return transactionManager;
    }
#else
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource#if($framework.contains('hibernate')), SessionFactory sessionFactory#{end}) throws Exception {
#if($framework.contains('hibernate'))
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
#else
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
#end
        return transactionManager;
    }
#end

#if($framework.contains('druid'))
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDbType(dbType);
        dataSource.setMinIdle(minIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setConnectionProperties(connectionProperties);
        dataSource.setValidationQuery(validationQuery);
        return dataSource;
    }
#end
#if($framework.contains('hikari'))
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
#end

#if($framework.contains('hibernate'))
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws Exception {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(new String[] {"${package}"});
//        factory.setConfigLocations("classpath:hibernate.cfg.xml");
//        factory.setMappingResources("*.hbm.xml");
        Properties hibernateProperties = new Properties();
        hibernateProperties.load(DalConfig.class.getClassLoader().getResourceAsStream("hibernate.properties"));
        factory.setHibernateProperties(hibernateProperties);
        return factory;
    }
#end
#if($framework.contains('mybatis'))
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
//        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//        Resource[] mappers = new Resource[mapperLocations == null ? 0 : mapperLocations.length];
//        for(int i = 0; i < mappers.length; i++) {
//            try {
//                mappers[i] = resourceResolver.getResources(mapperLocations[i]);
//            } catch (IOException e) {
//                //ignore
//            }
//        }
//        factory.setMapperLocations(mapperLocations);
        return factory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        ExecutorType executorType = ExecutorType.SIMPLE;
        return new SqlSessionTemplate(sqlSessionFactory, executorType);
    }
#end

## // refer: org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}