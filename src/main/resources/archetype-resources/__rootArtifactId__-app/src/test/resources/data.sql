INSERT INTO meta_class (id, class_name, super_class_name) VALUES (9547, 'java.beans.beancontext.BeanContextSupport', 'java.beans.beancontext.BeanContextChildSupport');

#if($showComment)
-- refer: org.springframework.beans.factory.support.PropertiesBeanDefinitionReader
#end
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('string', 'string.(class)','org.springframework.beans.factory.support.RootBeanDefinition');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('string', 'string.(abstract)','true ');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('string', 'string.scope','singleton');

INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('constructor-arg', 'constructor-arg.(class)','org.springframework.beans.factory.config.ConstructorArgumentValues');

INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('constructorRef', 'constructorRef.(class)','org.springframework.beans.factory.support.MethodOverrides');

INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('ref', 'ref.(class)','org.springframework.beans.MutablePropertyValues');

INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(class)','org.springframework.beans.factory.support.ChildBeanDefinition');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(parent)',' ');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(abstract)','false ');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(scope)','refresh');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(singleton)',' ');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.(lazy-init)','    ');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.$0','string');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.$1','**org.springframework.core.type.StandardClassMetadata');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.$2','*constructor-arg');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.$3(ref)','constructorRef');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.methodOverrides(ref)','ref');
INSERT INTO properties_bean_definition (bean_name, property_key, property_value) VALUES ('id', 'id.autowireMode','0');