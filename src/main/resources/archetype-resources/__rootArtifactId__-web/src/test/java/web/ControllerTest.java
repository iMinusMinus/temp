package ${package}.web;

import ${package}.BaseMvcTest;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ControllerTest extends BaseMvcTest {

    @Test
    public void testRest() {
        mvc.perform(MockMvcRequestBuilders.post("/dubbo/com.alibaba.dubbo.EchoService?version=1.0.0", "[{\"java.lang.String\":\"ok\"}]")).andReturn().getResponse().getContentAsString();
    }
}