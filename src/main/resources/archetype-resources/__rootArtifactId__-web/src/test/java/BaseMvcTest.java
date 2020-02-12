package ${package};

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/${servletName}-servlet.xml"})
public abstract class BaseMvcTest {

    @Value("${spring.profiles.active}")
    private String profile;

    @Resource
    private WebApplicationContext wac;

    protected MockMvc mvc;

    @Before
    public void setUp() {
        System.out.println("current run environment: " + profile);
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
     }

}