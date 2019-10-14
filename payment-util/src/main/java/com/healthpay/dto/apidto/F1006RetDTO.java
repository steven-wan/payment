package com.healthpay.dto.apidto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-09-17 14:44
 */
@XmlRootElement(name = "Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class F1006RetDTO {
    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
