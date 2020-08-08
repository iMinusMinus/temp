package ${package};

import ${package}.ws.ChinaOpenFund;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.util.Compiler;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.helpers.JavaUtils;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Webservice测试类
 */
public class WebServiceTest extends ContainerBase {

    public static class JaxWsDynaClientFactory extends JaxWsDynamicClientFactory {
        protected JaxWsDynaClientFactory(Bus bus) {
            super(bus);
        }
        public static JaxWsDynaClientFactory newInstance() {
            Bus bus = BusFactory.getThreadDefaultBus();
            return new JaxWsDynaClientFactory(bus);
        }
        @Override
        protected boolean compileJavaSrc(String classPath, List<File> srcList, String dest) {
            if (JavaUtils.isJava9Compatible()) {
                System.setProperty("org.apache.cxf.common.util.Compiler-fork", "true");
            }

            Compiler javaCompiler = new Compiler();
            javaCompiler.setClassPath(classPath);
            javaCompiler.setOutputDir(dest);
            javaCompiler.setEncoding("UTF-8");
            if (JavaUtils.isJava9Compatible()) {
                javaCompiler.setTarget("9");
            } else {
                javaCompiler.setTarget("1.8");
            }

            return javaCompiler.compileFiles(srcList);
        }
    }

    /**
     * 测试直接使用WSDL调用Webservice。
     * @throws Exception
     */
    @Test
    public void testCxfWsdl() throws Exception {
        //CXF使用javac进行编译，默认未设置源文件编码。在Windows系统下使用默认的GBK字符集时会报错
        //JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory newInstance();
        WebServiceTest.JaxWsDynaClientFactory factory = WebServiceTest.JaxWsDynaClientFactory.newInstance();
        //JAXB对"<s:element ref="s:schema"/>"处理有问题，保存到本地，替换成<s:any />
        //Client client = factory.createClient("http://ws.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx");
        Client client = factory.createClient("ChinaOpenFundWS.wsdl");
        Object[] params = {"userIdOrAnyNonNull"};
        Object[] result = client.invoke("getOpenFundDataSet",params);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.length > 0);
    }

    /**
     * 测试生成或编写SEI调用Webservice
     */
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