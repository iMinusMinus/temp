package ${package}.ws.dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "getOpenFundDataSetResponse")
@XmlType(name = "", propOrder = {"getOpenFundDataSetResult"})
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenFund implements Serializable {

    protected OpenFundData getOpenFundDataSetResult;

    @Getter
    @Setter
    @ToString
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"any"})
    public static class OpenFundData implements Serializable {

        @XmlAnyElement(lax = true)
        protected Object any;
//        /**
//         * 前单位净值
//         */
//        @XmlElement(name = "NetWorth_1")
//        private BigDecimal lastNetWorth;
//
//        /**
//         * 单位净值
//         */
//        @XmlElement(name = "NetWorth_2")
//        private BigDecimal currentNetWorth;
//
//        /**
//         * 累计单位净值
//         */
//        @XmlElement(name = "NetWorth_3")
//        private BigDecimal accruedNetWorth;
//
//        /**
//         * 净值涨跌额
//         */
//        @XmlElement(name = "WorthUp")
//        private BigDecimal netWorthChange;
//
//        /**
//         * 净值增长率（%）
//         */
//        @XmlElement(name = "WorthPercent")
//        private int netWorthRate;
//
//        /**
//         * 净值日期
//         */
//        @XmlElement(name = "WorthDate")
//        private Date worthDate;
//
//        /**
//         * 数据更新时间
//         */
//        @XmlElement(name = "ModifyDate")
//        private Date modifyDate;
    }

}