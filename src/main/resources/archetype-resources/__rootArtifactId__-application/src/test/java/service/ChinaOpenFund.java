package ${package}.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.ResponseWrapper;

import ${package}.dto.ObjectFactory;
import ${package}.dto.OpenFund;
import ${package}.dto.OpenFundBase;

/**
 * @see wsdl2java生成的结果
 * @author iMinusMinus
 * @date 2019-07-03
 */
@WebService(targetNamespace = "http://WebXml.com.cn/", name = "ChinaOpenFundWSSoap12"/*ChinaOpenFundWSHttpGet,ChinaOpenFundWSHttpPost,ChinaOpenFundWSSoap,*/)
@XmlSeeAlso({ObjectFactory.class})//jaxb hint for resolve xml any element.
public interface ChinaOpenFund {

    /**
     * 获得中国开放式基金的基金代号和基金名称DataSet。
     * @return
     */
    @WebMethod(action = "http://WebXml.com.cn/getFundCodeNameDataSet")
    @ResponseWrapper(localName = "getFundCodeNameDataSetResponse", targetNamespace = "http://WebXml.com.cn/")
    @WebResult(name = "getFundCodeNameDataSetResult", targetNamespace = "http://WebXml.com.cn/")
    OpenFundBase.OpenFundData getFundCodeNameDataSet();

    /**
     * 获得全部中国开放式基金数据DataSet。（免费用户只能获得最新 10 条数据）
     * @param userId 商业用户ID。免费用户不需要输入 userID 参数
     * @return 免费用户只能获得最新 10 条数据
     */
    @WebMethod(action = "http://WebXml.com.cn/getOpenFundDataSet")
    @ResponseWrapper(localName = "getOpenFundDataSetResponse", targetNamespace = "http://WebXml.com.cn/", className = "${package}.dto.OpenFund")
    @WebResult(name = "getOpenFundDataSetResult", targetNamespace = "http://WebXml.com.cn/")
    OpenFund.OpenFundData getOpenFundDataSet(@WebParam(name = "userID", targetNamespace = "http://WebXml.com.cn/") String userId);

}