package ${package}.api;

import ${package}.api.except.GenericException;

import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author iMinusMinus
 * @date 2019-05-03
 */
#if($showComment)
#*
// name属性默认为类名（不含包名部分），targetNamespace默认为协议后附加包名调整后的域名，wsdlLocation默认为协议、主机名、端口、servlet上下文、发布地址以及"?wsdl"组成.
// SEI(Service Endpoint Interface)不需要serviceName、endpointInterface、portName
*#
#end
#set($ns = "iamwhatiam.ml")
@WebService(name = "GenericService", targetNamespace = "http://api.${ns}", wsdlLocation = "http://api.${ns}/services?wsdl")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public interface GenericService {

    /**
     * invoke method of remote interface with specified types and arguments
     * @param methodName method name
     * @param parameterTypes parameter types
     * @param args parameters
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "$invoke", action = "", exclude = false)
    @WebResult(name = "return")
    // @WebFault(name = "GenericException")
    Object $invoke(@WebParam(name = "arg0", targetNamespace = "http://api.${ns}", mode = Mode.IN, header = false) String methodName,
                   @WebParam String[] parameterTypes, @WebParam Object[] args) throws GenericException;

}