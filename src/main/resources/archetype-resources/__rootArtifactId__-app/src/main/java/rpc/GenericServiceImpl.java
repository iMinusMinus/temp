package ${package}.rpc;

import ${package}.api.GenericService;
import ${package}.api.except.GenericException;

#if($configType.contains('@'))
#if($framework.contains('dubbo'))
import org.apache.dubbo.config.annotation.DubboService;
#else
import org.springframework.stereotype.Service;
#end
#end

/**
 * A ref bean proxy to variety interfaces!
 * @author iMinusMinus
 * @date 2019-05-03
 */
#if($configType.contains('@'))
#if($framework.contains('dubbo'))
@DubboService(application = "${dubboApplicationConfigBeanName}")
#else
@Service
#end
#end
public class GenericServiceImpl implements GenericService {

    private Object target;

    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        //TODO
        return null;
    }

}