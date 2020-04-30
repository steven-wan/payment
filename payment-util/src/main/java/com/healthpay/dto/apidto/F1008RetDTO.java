package com.healthpay.payment.account.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author steven-wan
 * @desc
 * @date 2020-04-30 11:34
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class F1008RetDTO{
    private String page;

    @XmlElement(name = "deposit_type")
    private Integer depositType;

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
