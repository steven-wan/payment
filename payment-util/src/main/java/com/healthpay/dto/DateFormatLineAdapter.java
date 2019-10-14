package com.healthpay.dto;

import org.apache.commons.lang3.time.DateUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-12 17:09
 */
public class DateFormatLineAdapter extends XmlAdapter<String, Date> {
    public DateFormatLineAdapter() {
    }

    public Date unmarshal(String v) throws Exception {
        return DateUtils.parseDate(v, new String[]{"yyyy-MM-dd HH:mm:ss"});
    }

    public String marshal(Date v) throws Exception {
        return format(v, "yyyy-MM-dd HH:mm:ss");
    }
    public static final String format(Object date, String pattern) {
        return date == null?null:(pattern == null?format(date):(new SimpleDateFormat(pattern)).format(date));
    }

    public static final String format(Object date) {
        return format(date, "yyyy-MM-dd");
    }
}
