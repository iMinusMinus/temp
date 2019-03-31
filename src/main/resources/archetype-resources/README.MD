## Refer
1. [Velocity](http://velocity.apache.org/engine/devel/user-guide.html)
## TODO
自定义clean plugin，移除明确不使用的模板

## Dependency Tree

1. Build

   + ANT
   
     build.xml, build.properties
     
   + Maven
   
     pom.xml, .mvn/jvm.config
     
   + Gradle
   
     build.gradle, gradle.properties  
     
2. Modules

   + web
   
     For Handling HTTP request.
     src/main/webapp/WEB-INF/jsp/,src/main/webapp/WEB-INF/tags/,src/main/webapp/WEB-INF/web.xml
   
   + api
   
     For exposing service to remote client request by RPC.
   
   + service
   
   + dal
   
     Domain in DDD.

3. Frameworks     

   + Spring MVC
   
     *-servlet.xml,applicationContext.xml,application.properties,views.properties
    
   + Struts2
   
     struts.xml
    
   + MyBatis
   
     mybatis-configuration.xml  
     
   + JValidation
   
     ValidationMessages.properties
     
   + Log
   
     log4j: log4j.properties
     log4j2: log4j2.yaml
     logback: logback.xml    
