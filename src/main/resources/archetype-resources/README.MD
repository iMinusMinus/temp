## Refer
1. [Velocity](http://velocity.apache.org/engine/devel/user-guide.html)

## Usage

- Maven

   ```sh
   mvn clean install
   mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=ml.iamwhatiam.archetype -DarchetypeArtifactId=temp -DarchetypeVersion=1.0-SNAPSHOT
   ```

## Dependency Tree

1. Build

   + ANT

     build.xml, build.properties

   + Maven

     pom.xml, .mvn/jvm.config

   + Gradle

     settings.gradle, build.gradle, gradle.properties  

2. Modules

   + app

     __For Handling HTTP request using Servlet:__
     
     src/main/webapp/WEB-INF/web.xml
     
     src/main/webapp/WEB-INF/jsp/
     
     src/main/webapp/WEB-INF/tags/
     
     src/main/resources/META-INF/context.xml
     
     src/main/resources/META-INF/resources/**/\*.[(html)|(js)|(css)|(png)|(jpg)|(gif)|(jpeg)]
     
     src/main/resources/META-INF/web-fragment.xml
     
     src/main/webapp/WEB-INF/extensions   
     
     src/main/webapp/WEB-INF/modules   
     
     src/main/webapp/WEB-INF/services   
     
     src/main/webapp/WEB-INF/tld   
     
     __For Handling HTTP request using Portlet:__
     
     src/main/webapp/WEB-INF/portlet.xml  
     
     *src/test/java/web/Portlet\*.java*
   
   + api

     __For exposing service to remote client request by RPC.__
     
     src/main/java/**/\*.java
   
   + domain
 
      __Domain in DDD.__
      
      src/main/java/
      
      src/main/resources/
      
      src/test/java/
      
      src/test/resources/

3. Frameworks

   + Spring
   
     src/main/webapp/WEB-INF/applicationContext.xml
     
     src/main/resources/META-INF/spring.*
     
     *src/test/resources/caucho.xml*
     
     src/test/resources/data.sql
     
     src/test/resources/schema.sql

   + Spring Web MVC

     src/main/webapp/WEB-INF:
     \*-servlet.xml  
     
     src/main/resources:
     application*.properties, messages.properties, messages_[a-z]{2}_[A-Z]{2}.properties, views.properties
   
   + Spring Web MVC Portlet
   
     src/main/webapp/WEB-INF/*-portlet.xml
    
   + Struts2

     src/main/resources:
     struts.xml, struts.properties
     
     src/test/resources:
     *struts2-spring.xml*

   + CXF

     src/main/webapp/WEB-INF/cxf-servlet.xml
     
     *src/test/resources/ChinaOpenFundWS.wsdl*
     
   + Netflix OSS
   
     *src/main/java/NetflixOSSConfig.java*
   
     archaius: src/main/resources/config.properties
    
     eureka: src/main/resources/eureka-client*.properties

   + Dubbo

     src/main/resources/dubbo*  
     
     src/main/resource/META-INF/services/org.apache.dubbo.*

   + MyBatis

     src/main/resources:
     *mybatis-config.xml,sqlmap/**/\*.xml*

   + Hibernate

     src/main/resources:
     hibernate.properties, hibernate.cfg.xml, **/\*.hbm.xml
     
   + JPA
   
     src/main/resources/META-INF/persistence.xml        

   + JValidation

     src/main/resources/ValidationMessages.properties
     
   + RabbitMQ
   
     *src/main/resources/rabbitmq\*.xml*
     
   + Kafka
   
     *src/main/resources/kafka\*.xml*
     
   + Quartz
   
     src/main/resources/quartz.properties  
     
   + Log

     JUL: src/main/resources/logging.properties   
     
     log4j2: src/main/resources/log4j2*, src/test/resources/log4j2*
        
     logback: src/main/resources/logback.xml,src/test/resources/logback-test.xml    
