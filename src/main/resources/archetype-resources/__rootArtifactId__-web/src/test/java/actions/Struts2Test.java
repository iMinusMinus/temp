package ${package}.actions;

import ${package}.actions.Struts2Action;

import org.apache.struts2.StrutsSpringTestCase;
import com.opensymphony.xwork2.ActionProxy;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import java.util.HashMap;

public class Struts2Test extends StrutsSpringTestCase {

    @Test
    public void testAction() throws Exception {
        long now = System.currentTimeMillis();
        request.setParameter("threadUnsafeVar", String.valueOf(now));
        executeAction("/test/execute");
        System.out.println("IF YOU SEE " + now + "IN CONSOLE, THIS TEST PASSED");
    }

    @Test
    public void testInheritAction() throws Exception {
        System.out.println("testInheritAction");
        request.setParameter("from.name", "form");
        request.setParameter("form.elements[0].type", "input");
        request.setParameter("form.elements[0].name", "username");
        request.setParameter("form.elements[0].value", "test");
        ActionProxy ap = getActionProxy("/struts2/alias");
        Struts2Action action = (Struts2Action) ap.getAction();
        ap.execute();
        Assert.assertEquals("test", action.getForm().getElements().get(0).getValue());
        action.setForm(null);//clean
    }

    //1. public void validate() {}
    //2. validation intercetor: {ActionClass}-validation.xml
    //3. intercetor use bean validation plugin
    @Test
    public void testBeanValidationPlugin() throws Exception {
        System.out.println("testBeanValidationPlugin");
        ActionProxy ap = getActionProxy("/struts2/alias");
        Struts2Action action = (Struts2Action) ap.getAction();
        System.out.println(ap.execute());
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
        return new String[]{"file:src/main/webapp/WEB-INF/applicationContext.xml"};
    }

}