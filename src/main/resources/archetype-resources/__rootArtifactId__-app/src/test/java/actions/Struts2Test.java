package ${package}.actions;

import lombok.extern.slf4j.Slf4j;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.StrutsSpringTestCase;
import com.opensymphony.xwork2.ActionProxy;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

@Slf4j
public class Struts2Test extends StrutsSpringTestCase {

    @Test
    public void testAction() throws Exception {
        ActionMapping am = getActionMapping("/struts2/notThreadSafe");
        Assert.assertEquals("/struts2", am.getNamespace());
        Assert.assertEquals("notThreadSafe", am.getName());
        long now = System.currentTimeMillis();
        log.info("action parameter: {}", now);
        request.setParameter("threadUnsafeVar", String.valueOf(now));
        String contentAsString = executeAction("/struts2/notThreadSafe");
        Assert.assertTrue("includeProperties 'threadUnsafeVar' not exist!", contentAsString.contains("threadUnsafeVar"));
        log.info("action output: {}", contentAsString);
    }

    // beanValidation:
    // 1. public void validate() {}
    // 2. validation interceptor: {ActionClass}-validation.xml
    // 3. interceptor use bean validation plugin
    @Test
    public void testPlugin() throws Exception {
        request.setParameter("threadUnsafeVar", String.valueOf(System.currentTimeMillis()));

        request.setParameter("form.elements[0].type", "input");
        request.setParameter("form.elements[0].name", "username");
        request.setParameter("form.elements[0].value", "test");
        ActionProxy ap = getActionProxy("/struts2/beanValidation");
        String result = ap.execute();
#if($configType.contains("xml"))
        Assert.assertEquals(com.opensymphony.xwork2.Action.INPUT, result);
#end
        request.setParameter("form.name", "form");
        ap = getActionProxy("/struts2/beanValidation");
        Struts2Action action = (Struts2Action) ap.getAction();
        Assert.assertEquals(com.opensymphony.xwork2.Action.SUCCESS, ap.execute());
        Assert.assertEquals("test", action.getForm().getElements().get(0).getValue());
        action.setForm(null);//clean
    }

    //fix Couldn't get resource paths for class path resource [WEB-INF/jsp/]
    @Override
    protected void initServletMockObjects() {
        this.servletContext = new MockServletContext(resourceLoader){
            @Override
            protected String getResourceLocation(String path) {
                if(path.startsWith("/WEB-INF/")) {
                    path = "file:src/main/webapp" + path;
                } else if (!path.startsWith("/")) {
                    path = "/" + path;
                }

                return path;
            }
        };
        this.response = new MockHttpServletResponse();
        this.request = new MockHttpServletRequest();
        this.pageContext = new MockPageContext(this.servletContext, this.request, this.response);
    }

    @Override
    protected String[] getContextLocations() {
        //return new String[]{"file:src/main/webapp/WEB-INF/applicationContext.xml"};
        return new String[] {"classpath:struts2-spring.xml"};
    }

}