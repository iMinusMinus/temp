package ${package}.rpc;

import ${package}.api.GenericService;
import ${package}.api.except.GenericException;

import org.springframework.stereotype.Service;

/**
 * A ref bean proxy to variety interfaces!
 * @author iMinusMinus
 * @date 2019-05-03
 */
@Service
public class GenericServiceImpl implements GenericService {

    private Object target;

    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        //TODO
        return null;
    }

}