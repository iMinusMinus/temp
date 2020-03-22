package ${package}.rpc;

import ${package}.api.GenericService;
import ${package}.api.except.GenericException;

#if($framework.contains('dubbo') and $configType.contains('@'))
import org.apache.dubbo.config.annotation.Service;
#end

/**
 * A ref bean proxy to variety interfaces!
 * @author iMinusMinus
 * @date 2019-05-03
 */
#if($framework.contains('dubbo') and $configType.contains('@'))
@Service
#end
public class GenericServiceImpl implements GenericService {

    private Object target;

    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        //TODO
        return null;
    }

}