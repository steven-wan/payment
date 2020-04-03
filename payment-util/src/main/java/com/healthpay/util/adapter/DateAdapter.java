package com.healthpay.util.adapter;



import com.healthpay.util.adapter.dateUtil.DateUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

/**
 * @author steven-wan
 * @desc
 * @date 2020-04-03 09:13
 */
public class DateAdapter  extends XmlAdapter<String, Date> {
    public DateAdapter() {
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return DateUtil.formatDateStr(v, "yyyyMMddHHmmss");
    }

    @Override
    public String marshal(Date v) throws Exception {
        return DateUtil.format(v, "yyyyMMddHHmmss");
    }
}