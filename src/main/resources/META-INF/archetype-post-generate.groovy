import java.io.File
import java.sql.Array;
import java.util.Arrays;

File root = new File(request.getOutputDirectory())

// locate project base dir
File[] resources = null
File[] projectDirFiles = root.listFiles()
for(int i = 0; i < projectDirFiles.length; i++) {
    if(projectDirFiles[i].isDirectory() && projectDirFiles[i].getName().equals(request.properties.artifactId)) {
        resources = projectDirFiles[i].listFiles()
        break
    }
}
File[] legacyConfigResources = new File[0]
// clean unused build file
for(int i = 0; i < resources.length; i++) {
    if(!resources[i].isDirectory()) {
        if(!request.properties.buildTool.contains("Gradle")) {
            List<String> gradleResources = Arrays.asList("settings.gradle", "build.gradle", "gradle.properties")
            if(gradleResources.contains(resources[i].getName())) {
                resources[i].delete()
            }
        }
        if(!request.properties.buildTool.contains("ANT")) {
            List<String> antResources = Arrays.asList("build.xml", "build.properties")
            if(antResources.contains(resources[i].getName())) {
                resources[i].delete()
            }
        }
    }
    if(resources[i].isDirectory()) {
        File subProjectGradle = new File(resources[i].getPath() + "/build.gradle")
        File subProjectANT = new File(resources[i].getPath() + "/build.xml")
        if(!request.properties.buildTool.contains("Gradle") && subProjectGradle.exists()) {
            subProjectGradle.delete()
        }
        if(!request.properties.buildTool.contains("ANT") && subProjectANT.exists()) {
            subProjectANT.delete()
        }
    }
    // assign to ${rootArtifactId}-app/src/main/resources
    if(resources[i].isDirectory() && resources[i].getName().endsWith("-app")) {
        File xmlConfig = new File(resources[i].getPath() + "/src/main/resources")
        legacyConfigResources = xmlConfig.listFiles()
    }

}

//clean unused xml
for(int j = 0; j < legacyConfigResources.length; j++) {
    List<String> xmlResources = new ArrayList<>()
    xmlResources.add("mybatis-config.xml")
    List<String> dubboXmlConfigs = Arrays.asList("dubbo.xml", "dubbo-consumer.xml", "dubbo-provider.xml")
    List<String> kafkaXmlConfigs = Arrays.asList("kafka.xml", "kafka-consumer.xml", "kafka-producer.xml")
    List<String> rabbitMQXmlConfigs = Arrays.asList("rabbitmq.xml", "rabbitmq-publisher.xml")
    xmlResources.addAll(dubboXmlConfigs)
    xmlResources.addAll(kafkaXmlConfigs)
    xmlResources.addAll(rabbitMQXmlConfigs)
    if(!request.properties.configType.contains("xml")) {
        if(xmlResources.contains(legacyConfigResources[i].getName())) {
            legacyConfigResources[i].delete()
            xmlResources.remove(legacyConfigResources[i].getName())
        }
    }
    if(!request.properties.framework.contains("dubbo")) {
        if(dubboXmlConfigs.contains(legacyConfigResources[i].getName())) {
            legacyConfigResources[i].delete()
            xmlResources.remove(legacyConfigResources[i].getName())
        }
    }
    if(!request.properties.framework.contains("kafka")) {
        if(kafkaXmlConfigs.contains(legacyConfigResources[i].getName())) {
            legacyConfigResources[i].delete()
            xmlResources.remove(legacyConfigResources[i].getName())
        }
    }
    if(!request.properties.framework.contains("rabbitmq")) {
        if(rabbitMQXmlConfigs.contains(legacyConfigResources[i].getName())) {
            legacyConfigResources[i].delete()
            xmlResources.remove(legacyConfigResources[i].getName())
        }
    }
}
