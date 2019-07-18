package ${package}.service;

import ${package}.BaseAppTest;
import ${package}.service.ChinaOpenFund;
import ${package}.api.EchoService;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import org.junit.Test;

public class WebServiceTest extends BaseAppTest {

//    @Test
//    public void testCxfWsdl() throws Exception {//JAXB对"<s:element ref="s:schema"/>"处理有问题
//        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
//        Client client = factory.createClient("http://ws.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx");
//        System.out.println(client.invoke("getOpenFundDataSet",null));
//    }

    @Test
    public void testCxfJaxWs() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress("http://ws.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx");
//        factory.setServiceClass(ChinaOpenFund.class);
//        ChinaOpenFund ws = (ChinaOpenFund) factory.create();
        ChinaOpenFund ws = factory.create(ChinaOpenFund.class);
        System.out.println(ws.getFundCodeNameDataSet());
    }


}