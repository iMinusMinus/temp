#if($showComment)
# 减少镜像体积
# 1. 使用"&&"连接多个命令，避免每个命令创建一层镜像
# 2. 使用多阶段构建
# 3. 使用alpine等体积小的基础镜像
#end
FROM 127.0.0.1:5000/pi/maven:3 AS stage0
$MAVEN_HOME/bin/mvn clean package -Dmaven.test.skip=true

FROM 127.0.0.1:5000/pi/tomcat:9
MAINTAINER iMinusMinus <mean.leung@outlook.com>
COPY --from=stage0 target/*.war $CATALINA_HOME/webapps/
EXPOSE 8080
VOLUME /var/log