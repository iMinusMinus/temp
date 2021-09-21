package ${package};

import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
#{if}($parentContextKey)
    @ContextConfiguration(classes = {${package}.NetflixOSSConfig.class}),
#{end}
    @ContextConfiguration(#{if}($configType.contains('xml'))locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"}#{else}classes = {${package}.ContextConfig.class}#{end}),
    @ContextConfiguration(#{if}($configType.contains('xml'))locations = {"file:src/main/webapp/WEB-INF/${servletName}-servlet.xml"}#{else}classes = {${package}.MvcConfig.class}#{end})
})
public abstract class ContainerBase {

    @Resource
    private WebApplicationContext wac;

    protected MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

}