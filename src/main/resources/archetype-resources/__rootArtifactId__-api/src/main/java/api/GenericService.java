package ${package}.api;

import ${package}.api.except.GenericException;

/**
 * @author iMinusMinus
 * @date 2019-05-03
 */
public interface GenericService {

    /**
     * invoke method of remote interface with specified types and arguments
     * @param methodName method name
     * @param parameterTypes parameter types
     * @param args parameters
     * @return
     * @throws GenericException
     */
    Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException;

}