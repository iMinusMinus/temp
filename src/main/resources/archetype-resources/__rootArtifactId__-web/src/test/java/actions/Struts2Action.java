package ${package}.actions;

import ${package}.vo.Form;

import lombok.Getter;
import lombok.Setter;

import com.opensymphony.xwork2.Action;
#if(!$configType.contains('@java'))
import com.opensymphony.xwork2.ActionSupport;
#else
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
#end

#if(!$configType.contains('xml'))
@Namespace("/struts2")
@Results({@Result(name = "success", type="json")})
#end
@Getter
@Setter
public class Struts2Action #if($configType.contains('xml'))extends ActionSupport#{end} {

    private String threadUnsafeVar;

    private Form form;

#**
    REST
    //The method name to call for a GET  request with no id parameter
    public String index() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a GET  request with an id parameter
    public String show() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a POST  request with no id parameter
    public String create() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a PUT  request with an id parameter
    public String update() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a DELETE  request with an id parameter
    public String destroy() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a GET  request with an id parameter and the edit view specified
    public String edit() {
        //TODO
        return Action.SUCCESS;
    }
    //The method name to call for a GET  request with no id parameter and the new view specified
    public String editNew() {
        //TODO
        return Action.SUCCESS;
    }
*#

#if(!$configType.contains('xml'))
    @Action("/test/execute")
#end
    public String doExecute() {
        System.out.println(threadUnsafeVar);
        return Action.SUCCESS;
    }

#if(!$configType.contains('xml'))
    @Actions({@Action("invoke"),@Action("alias")})
#end
    public String doInvoke() {
        System.out.println(form);
        return Action.SUCCESS;
    }
}