package ${package}.dto;

import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName DATA_SET_QNAME = new QName("http://WebXml.com.cn/", "DataSet");

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataSet }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataSet }{@code >}
     */
    @XmlElementDecl(namespace = "http://WebXml.com.cn/", name = "DataSet")
    public JAXBElement<DataSet> createDataSet(DataSet value) {
        return new JAXBElement<DataSet>(DATA_SET_QNAME, DataSet.class, null, value);
    }

}