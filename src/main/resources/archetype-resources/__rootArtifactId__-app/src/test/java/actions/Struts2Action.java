package ${package}.actions;

import ${package}.api.EchoService;
import ${package}.vo.Form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

#if($configType.contains('xml'))
import com.opensymphony.xwork2.ActionSupport;
#else
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
#end

#if(!$configType.contains('xml'))
@Namespace("/struts2")
@ParentPackage(value = "json-default") // 无法使用多个@ParentPackage，或value包含多个
@ResultPath("/WEB-INF/jsp/")
@Results({@Result(name = "none", type="dispatcher", location = "404.jsp"), @Result(name = "error", type = "dispatcher", location = "error.jsp")})
#end
@Getter
@Setter
public class Struts2Action #if($configType.contains('xml'))extends ActionSupport#{end} {

    private String threadUnsafeVar;

    @Valid
    private Form form;

## // spring managed bean and inject it.
    private EchoService echoService;

#**
    // REST

    private String id;

    // The method name to call for a GET  request with no id parameter
    public String index() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a GET  request with an id parameter
    public String show() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a POST  request with no id parameter
    public String create() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a PUT  request with an id parameter
    public String update() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a DELETE  request with an id parameter
    public String destroy() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a GET  request with an id parameter and the edit view specified
    public String edit() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
    // The method name to call for a GET  request with no id parameter and the new view specified
    public String editNew() {
        //TODO
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
*#

#if(!$configType.contains('xml'))
    @Action(value = "notThreadSafe", results = @Result(name = "success", type = "json", params = {"includeProperties", "threadUnsafeVar"}))
#end
    public String doExecute() {
        System.out.println(threadUnsafeVar);
        return com.opensymphony.xwork2.Action.SUCCESS;
    }

## // refer: com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor
## // Action可以通过实现ValidationWorkflowAware、ValidationErrorAware来实现自定义视图名称（默认为input），也可以通过InputConfig注解自定义视图


#if(!$configType.contains('xml'))
    @Actions({@Action(value = "beanValidation", results = {@Result(name = "success", type = "json", params = {"root", "form"}), @Result(name = "input", type = "httpheader", params = {"status", "400"})}), @Action("spring")})
#end
    public String doInvoke() {
        System.out.println(form);
        echoService.echo(null);
        return com.opensymphony.xwork2.Action.SUCCESS;
    }
}