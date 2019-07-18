package ${package}.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"getFundCodeNameDataSetResult"})
@XmlRootElement(name = "getFundCodeNameDataSetResponse")
public class OpenFundBase implements Serializable {

    @XmlAttribute(name = "getFundCodeNameDataSetResult")
    protected OpenFundBase.OpenFundData dataSet;

    @Getter
    @Setter
    @ToString
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"any"})
    public static class OpenFundData implements Serializable{

        @XmlAnyElement(lax = true)
        protected Object any;
//        /**
//         * 基金代号
//         */
//        @XmlElement(name = "FundCode")
//        protected String fundCode;
//
//        /**
//         * 基金名称
//         */
//        @XmlElement(name = "FundName")
//        protected String fundName;
    }

}