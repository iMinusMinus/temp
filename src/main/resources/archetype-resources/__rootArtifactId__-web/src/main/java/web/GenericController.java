package ${package}.web;

import ${package}.api.GenericService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/{rpc}")
public class GenericController {

    @Resource
    private GenericService genericService;

    @RequestMapping(value = "/{service}", method = {RequestMethod.POST})
    @ResponseBody
    public Object invoke(@PathVariable("rpc") String rpc, @PathVariable("service") String service,
                             @RequestParam(value="version", required=false) String version,
                             @RequestBody String parameters) {
        //TODO
        return null;
    }

}