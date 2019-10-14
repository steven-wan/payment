package com.healthpay.dto.httpbodydto;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * @desc 接收商户发送过来的数据
 * @author gyp
 * @version 1.0
 * @date 2016-09-08 19:16
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "header", "body" })
public class DataDTO {
    @Valid
    @XmlElement(name = "head")
    private HeaderDTO header;

    @NotNull(message = "{body_is_null}")
    @XmlElement(name = "body")
    @XmlCDATA
    private String body;

    public HeaderDTO getHeader() {
        return header;
    }

    public void setHeader(HeaderDTO header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
