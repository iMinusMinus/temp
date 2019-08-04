package ${package}.actions;

import ${package}.actions.Struts2Action;

import org.apache.struts2.StrutsSpringTestCase;
import com.opensymphony.xwork2.ActionProxy;

import org.junit.Assert;
import org.junit.Test;

public class Struts2Test extends StrutsSpringTestCase {

    @Test
    public void testAction() throws Exception {
        request.setParameter("threadUnsafeVar", "testValue");
        executeAction("/test/execute");
    }

    @Test
    public void testInheritAction() throws Exception {
        request.setParameter("from.name", "form");
        request.setParameter("form.elements[0].type", "input");
        request.setParameter("form.elements[0].name", "username");
        request.setParameter("form.elements[0].value", "test");
        ActionProxy ap = getActionProxy("/struts2/alias");
        Struts2Action action = (Struts2Action) ap.getAction();
        ap.execute();
        Assert.assertEquals("test", action.getForm().getElements().get(0).getValue());
    }

    @Override
    protected String[] getContextLocations() {
        return new String[]{"file:src/main/webapp/WEB-INF/applicationContext.xml"};
    }

}