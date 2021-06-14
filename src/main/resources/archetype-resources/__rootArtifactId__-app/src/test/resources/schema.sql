#if($showComment)
-- refer: org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
#end
CREATE TABLE meta_class (id BIGINT NOT NULL, class_name VARCHAR(65535), super_class_name VARCHAR(255), PRIMARY KEY (id));

#if($showComment)
-- refer: org.springframework.jdbc.core.support.JdbcBeanDefinitionReader
#end
CREATE TABLE properties_bean_definition (id BIGINT NOT NULL AUTO_INCREMENT, bean_name VARCHAR(64) NOT NULL, property_key VARCHAR(255) NOT NULL, property_value VARCHAR(2000), PRIMARY KEY (id));