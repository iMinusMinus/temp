## Refer
1. [Velocity](http://velocity.apache.org/engine/devel/user-guide.html)

## Dependency Tree

1. Build

   + ANT

     build.xml, build.properties

   + Maven

     pom.xml, .mvn/jvm.config

   + Gradle

     settings.gradle, build.gradle, gradle.properties  

2. Modules

   + web

     For Handling HTTP request.
     src/main/webapp/WEB-INF/jsp/,src/main/webapp/WEB-INF/tags/,src/main/webapp/WEB-INF/web.xml
     src/main/webapp/WEB-INF/extensions   
     src/main/webapp/WEB-INF/modules   
     src/main/webapp/WEB-INF/services   
     src/main/webapp/WEB-INF/tld   
   
   + api

     For exposing service to remote client request by RPC.
   
   + domain
 
      Domain in DDD.

3. Frameworks     

   + Spring MVC

     src/main/webapp/WEB-INF:
     *-servlet.xml,applicationContext.xml   
     src/main/resources:
     application*.properties,views.properties
    
   + Struts2

     struts.xml

   + CXF

     src/main/webapp/WEB-INF/cxf-servlet.xml

   + Dubbo

     src/main/resources/dubbo*  

   + MyBatis

     src/main/resources/mybatis-config.xml,src/main/resources/sqlmap 

   + Hibernate

     src/main/resources/hibernate.cfg.xml,src/main/resources/**/*.hbm.xml
     
   + JPA
   
     src/main/resources/META-INF/persistence.xml        

   + JValidation

     src/main/resources/ValidationMessages.properties
     
   + RabbitMQ
   
     src/main/resources/rabbitmq*.xml
     
   + Kafka
   
     src/main/resources/kafka*.xml

   + Log

     JUL: src/main/resources/logging.properties   
     log4j2: src/main/resources/log4j2.yaml   
     logback: src/main/resources/logback.xml,src/test/resources/logback-test.xml    
