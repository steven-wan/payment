package com.healthpay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.healthpay.dto.Constants;
import com.healthpay.dto.apidto.*;
import com.healthpay.dto.httpbodydto.DataDTO;
import com.healthpay.dto.httpbodydto.HeaderDTO;
import com.healthpay.httpUtil.HttpClientUtil;
import com.healthpay.securityUtil.SecurityUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-11 16:40
 */
public class healthPayUtil {
    /**
     * 访问健保付聚合支付
     *
     * @param url         访问地址
     * @param data        接口的data
     * @param funcId      接口文档中的每个接口的代号
     * @param privateKey  私钥
     * @param developerId 开发者ID
     * @param desKey      对称秘钥
     * @return
     * @throws Exception
     */
    public static String httpPostToJbfPay(String url, String data, String funcId, String privateKey, String developerId, String desKey, String publicKey) throws Exception {
        String rquestBody = httpRquestBodyToJbfPay(data, funcId, privateKey, developerId, desKey);
        String retXml = HttpClientUtil.httpPost(url, rquestBody, HttpClientUtil.CONTENT_TYPE);
        String responseBody = httpResponseBodyFromJbfPay(retXml, publicKey, desKey, funcId);
        return responseBody;
    }

    /**
     * 访问聚合支付请求报文组装
     *
     * @param data
     * @param funcId
     * @param privateKey
     * @param developerId
     * @param desKey
     * @return
     * @throws Exception
     */
    public static String httpRquestBodyToJbfPay(String data, String funcId, String privateKey, String developerId, String desKey) throws Exception {
        DataDTO dataDTO = new DataDTO();
        //header赋值
        HeaderDTO headerDTO = new HeaderDTO();
        headerDTO.setFuncId(funcId);
        headerDTO.setMerchantNo(developerId);
        headerDTO.setTimestamp(new Timestamp((new Date()).getTime()).getTime() + "");
        String sign = SecurityUtil.signRSA(data, privateKey, SecurityUtil.CHARSET);
        headerDTO.setSignature(sign);
        String body = SecurityUtil.encryptDes(data, desKey, SecurityUtil.CHARSET);
        dataDTO.setBody(body);
        dataDTO.setHeader(headerDTO);
        //进行ttp请求
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(dataDTO, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 访问聚合支付返回报文解析
     *
     * @param retXml
     * @param desKey
     * @param funcId
     * @return
     * @throws Exception
     */
    public static String httpResponseBodyFromJbfPay(String retXml, String publicKey, String desKey, String funcId) throws Exception {
        DataDTO retDataDto = JAXB.unmarshal(new StringReader(retXml), DataDTO.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", retDataDto.getHeader().getRetCode());
        jsonObject.put("msg", retDataDto.getHeader().getRetMsg());
        if (StringUtils.isNotBlank(retDataDto.getBody())) {
            String retBody = SecurityUtil.decryptDes(retDataDto.getBody(), desKey, "utf-8");
            if (!SecurityUtil.verifyRSA(retBody, publicKey, retDataDto.getHeader().getSignature(), SecurityUtil.CHARSET)) {
                jsonObject.put("msg", "验签失败");
            }
            if ("0000".equals(retDataDto.getHeader().getRetCode())) {
                Class classs;
                switch (funcId) {
                    case Constants.FUNC_ID_P2005:
                        classs = P2005RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P7001:
                        classs = P7001RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P7002:
                        classs = P7002RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P2009:
                        classs = P2009RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P2008:
                        classs = P2008RetDTO.class;
                        break;
                    case Constants.FUNC_ID_F1006:
                        classs = F1006RetDTO.class;
                        break;
                    case Constants.FUNC_ID_R1001:
                        classs = R1001RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P8001:
                        classs = AcPayRetDto.class;
                        break;
                    case Constants.FUNC_ID_P3001:
                        classs = MerCallbackDTO.class;
                        break;
                    case Constants.FUNC_ID_F1008:
                        classs = com.healthpay.payment.account.dto.F1008RetDTO.class;
                        break;
                    case Constants.FUNC_ID_P7003:
                    case Constants.FUNC_ID_P7004:
                        jsonObject.put("data", retBody);
                        return jsonObject.toJSONString();
                    default:
                        throw new Exception("错误的 funcId");
                }
                jsonObject.put("data", JAXB.unmarshal(new StringReader(retBody), classs));
            }
        }


        return jsonObject.toJSONString();
    }


    public static void main(String[] args) throws Exception {
        //face();
        test8001();
    }

    public static void test8001() throws Exception {
        try {
            String data = "<data>" +
                    "<order_no>" + System.currentTimeMillis() + "</order_no>" +
                    "<amt>0.01</amt>" +
                    "<merchant_no>1001003</merchant_no>" +
                    "<spbill_create_ip>192.168.11.12</spbill_create_ip>" +
                    "<personal_id>xw2020050801</personal_id>" +
                    "<user_name>steven</user_name>" +
                    "<mobile>13207899635</mobile>" +
                    "<user_id>oKnuNxPb8fFNcgWm4FRpTrwM4shA</user_id>" +
                    "<jump_url>https://music.163.com/#/song?id=188175</jump_url>" +
                    "<goods_id>充值</goods_id>" +
                    //"<rebot_position>0f048609d162464894da08c91fca873b</rebot_position>" +
                    "<timeout_express>30</timeout_express>" +
                    "<goods_name>充值Q币</goods_name>" +
                    "</data>";
            String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEA1y+vJaqxVg6DN9nEiO12JeKPPZuWOBnfKkCOO2XrsZCnnnvauZ4js3QKDhRgXTZQcIi2wEQW2h5uIdfvQzktPQIDAQABAkBt9J+6z43uO1wxncUfcrd8hFhNUsNfx1iRbos/LsVQ5Xm6/ausoaj16SlSuQViqdcblNDi5XmD3ftmqSpZs4YBAiEA82wl6YyerumoHZrQ/Q16dhAuRSccWHonw2dk8AbnBqUCIQDiTgtH0QoN/OgvBQqx/cpVYZuKfbVor0l6YSBOWZHguQIgXKYdB/dtrkVgp2P1h1tJ8QXXlpp8P3C/EbCYyoLWC8ECIQCavHhUG6e/Vr0/YTgl6f1OEhZzRG8k7C3WotCfIflv6QIgadkRO/9tpleO/ejUTKIxfPIHWVvm4j2q3oSFaiZSxYA=";
            String sign = SecurityUtil.signRSA(data, privateKey, "utf-8");
            String desKey = "O8F6XROoLMg=";
            String content = SecurityUtil.encryptDes(data, desKey, "utf-8");
            String xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<root>\n" +
                    "\t<head>\n" +
                    "\t\t<developer_id>75148</developer_id>\n" +
                    "\t\t<func_id>P8001</func_id>\n" +
                    "\t\t<timestamp>12312312312324</timestamp>\n" +
                    "\t\t<signature>" + sign + "</signature>\n" +
                    "\t</head>\n" +
                    "\t<body>" + content + "</body>\n" +
                    "</root>";
            //dev.jbf.aijk.net
            //clouddev.jbf.aijk.net
            //172.16.22.152:80:80
            String retXml = HttpClientUtil.httpPost("http://dev.jbf.aijk.net/gatewayOnline/gateway/portal/execute", xml, "application/xml");

            DataDTO dataDTO = JAXB.unmarshal(new StringReader(retXml), DataDTO.class);
            String retData = "";
            if (!"".equals(dataDTO.getBody())) {
                retData = SecurityUtil.decryptDes(dataDTO.getBody(), desKey, "utf-8");
            }
            System.out.println("输入报文:");
            System.out.println(data);
            if ("0000".equals(dataDTO.getHeader().getRetCode())) {
                System.out.println("返回报文:");
                System.out.println(retData);
            } else {
                System.out.println(dataDTO.getHeader().getRetMsg());
            }
        } catch (Exception e) {

        }
    }

    public static void callBack() throws Exception {

        //公钥1
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKxhFYJOUdRp0wY7QfntcJ3lVZ4G1hEMWNSSLGezzS9L8OV/NFy57AyhcHKYnLqw2ZSURo4xLHSNFjc4ASsi/PMCAwEAAQ==";

        String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtfVX2zjJ9JPVZ8R6T6NFwZWmF/XC\n" +
                "pfwHWbTu+tnqVl2fFQODfPOBbNtvrPyWi4m09x9P6F8HEoY6+gq/Iko9ewIDAQABAkAfsssIZL9B\n" +
                "/VMLDb5lC0OGsuRJfkXXlq1NImkTiEz7mG2uyX2mdkSdmhBxs1+kMnYSOmbsPBDvPKHvz0Qp7hdh\n" +
                "AiEA+h8jjdn3iaX2SGAIlo84pqpWTwjO2etz9bLVE5nMtKsCIQC6PBlOlq0PhobXtIzTTonS+fev\n" +
                "INX/M6NGO4AdMH96cQIgb0zp+lZzA4qZhG1PhQfocqm7zGGkAm724++XR6iZ4g8CIQC4Jt1PXKbc\n" +
                "B0Ym3Z2zBKI8QHiub2Wr6D+3Hvbb5iznwQIhAN+GkKT4x+rq7PMNGPYcQFrAlMDu3gFttMKjXwr/\n" +
                "6Eif";

        String desKey = "YXbW9BCoUeo=";
        String developerId = "64229";
        String funcId = "P3001";

        DataDTO dataDTO = new DataDTO();
        HeaderDTO headerDTO = new HeaderDTO();
        headerDTO.setFuncId(funcId);
        headerDTO.setMerchantNo(developerId);
        headerDTO.setRetCode("0000");
        headerDTO.setRetMsg("");
        headerDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));
        MerCallbackDTO merCallbackDTO = new MerCallbackDTO();
        // 平台交易流水号
        merCallbackDTO.setTradeNo("65455");

        // 获取数据

        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(merCallbackDTO, stringWriter);
        String xml = stringWriter.toString();
        System.out.println(xml);
        // 用对称秘钥加密
        String data = SecurityUtil.encryptDes(xml, desKey, "utf-8");
        dataDTO.setBody(data);
        // 签名
        String sign = SecurityUtil.signRSA(xml, privateKey, "utf-8");
        headerDTO.setSignature(sign);
        dataDTO.setHeader(headerDTO);
        StringWriter stringWriterbody = new StringWriter();
        JAXB.marshal(dataDTO, stringWriterbody);
        String bodyxml = stringWriterbody.toString();
        System.out.println(bodyxml);
        bodyxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "   <head>\n" +
                "      <developer_id>64229</developer_id>\n" +
                "      <func_id>P3001</func_id>\n" +
                "      <timestamp>1586264592967</timestamp>\n" +
                "      <signature><![CDATA[dJrmSc7e6znWDEDoGVzlHUI0JmZoMu00eK4oD197MXF3BnGspOYAGZn6l0letjYarrqx1obSnIPbIR3NKjZHdg==]]></signature>\n" +
                "      <ret_code>0000</ret_code>\n" +
                "      <ret_msg></ret_msg>\n" +
                "   </head>\n" +
                "   <body><![CDATA[KPCAKCHaAsLTP66mskFetId0fLfuKppc+IuwXwfdJCiZrwJOPk4cZ9jOAThM0BVqWF5PyJJFzEI8T5E30FRRkGHm3MMENnnpkmjXiyyYuAxHJumX727qKbU9p/5hhBp7nFosnt2M84Il+m46ASMWJkkD6Un7u9bbkRgQ9BjykZpEJA3vR2UMowypZ+EXeqFj7zzh8lXn2Wo5W/ZK+3XMTkkYQuxgbngVG+DNzM2hRBRkYMXLRtuXs2Rb7mXvJcvFZ9zr84+CXunpEl87C2q6QjBIIGv6lmQToCbnZ9B3O67QwVLgYP9yPNpGvDJOsva6KrztGW2lORSYwKSDzO4c5TjbROOK8+Wx0MFS4GD/cjz0mU5VB63DHDlZPejmCkZxrAwrZw2nGkbubRXvD09a8KAzURYx3tUf5lq9djAuO0RtSWVh7tgqtzHs8FW2Kac2fwxS+MT6o0z058gDCEyVoqbSF/t0JoVtGX+XG7S1id8=]]></body>\n" +
                "</root>";
        System.out.println(httpResponseBodyFromJbfPay(bodyxml, publicKey, desKey, funcId));
    }

    public static void pay() throws Exception {
        //http://172.16.104.91/gatewayOnline//gateway/portal/execute http://116.7.255.40:53501/gatewayOnline/gateway/portal/execute
        String url = "http://dev.jbf.aijk.net/gatewayOnline/gateway/portal/execute";

        String data =
                "<data>\n" +
                        "<request_no>1592379208289</request_no>\n" +
                        "<merchant_no>1001003</merchant_no>\n" +
                        "</data>";
        //私钥1
        String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAxgmkztQ7XR9fCZFyRoR2VIb213OHic9dJR9eUNHZ5cMxkvE+iKsWnPOJS9MLWbafN8D67x4FFbGdNzsE5rjYQwIDAQABAkAHKuowIjZT1ILYEc+VBc5SmN/uRFwvfLXeVhhPzIiabDnPbu9dck9am6c0inYh3FNmz1PuyT+1obCLBqFyMjBBAiEA9S+XWTEhnHwwskGcSQ1J3Wb1dZQMmd45tzjAbxa5OukCIQDOxbL015J02TSdK7T1ou6QQ1VetLwam+d5jzBIBEYmSwIhAO7I5KtWsBdI09wnDeAPcwqPBSr3frYzuR35xV/rT4ZRAiEAuYHbLRkel2CreHrW7i3QVHNkawRBYqoOwRt62JGlEgMCIQCV3lETvsvaBI80dBwWk4WQ+jySyJ4r9YHqlSROl2xCfg==";
        String desKey = "Sq3crjTCzYU=";
        String developerId = "18653";
        String funcId = "P2009";

        //公钥1
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJDoMWxU5V32lmODTUU8UuhWDbFe6oldxmfCmh736ePH0EC23J4WY+I//u5AJb4wXDfDf0JkYrN1vbBUFu3yb6ECAwEAAQ==";


        System.out.println("time1:" + System.currentTimeMillis());
        String resp = httpRquestBodyToJbfPay(data, funcId, privateKey, developerId, desKey);

        System.out.println("time2:" + System.currentTimeMillis());
        //System.out.println(SecurityUtil.verifyRSA(data,publicKey,"O6UkLlHOHveU37Ab0ismkH/HZdq45jtcSSk3idtrV2poazSsAlVJhFM+DWgeVJWCFiJdtjKlssxLh+XEPVZjnA==","utf-8"));
        System.out.println(resp);
        //公钥2
        publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM9C28yhUyP9Jluiq7YoLlSSLzUg5xa4EwqOic6mTrVA1NBUCcO/FgveKdvU/AFG2ZM+2FQNq3Gl31husBBBeaMCAwEAAQ==";
        System.out.println("time3:" + System.currentTimeMillis());
        resp = httpPostToJbfPay(url, data, funcId, privateKey, developerId, desKey, publicKey);
        //System.out.println(httpResponseBodyFromJbfPay(resp, publicKey, desKey, funcId));
        System.out.println("time4:" + System.currentTimeMillis());
        System.out.println(resp);
    }

    public static void face() throws Exception {
        //http://172.16.104.91/gatewayOnline//gateway/portal/execute http://116.7.255.40:53501/gatewayOnline/gateway/portal/execute

        //dev.jbf.aijk.net
        //clouddev.jbf.aijk.net
        //localhost:80
        String url = "http://172.16.21.17:8080/index.html#/intro?";
        SessionCodeDTO sessionCodeDTO = new SessionCodeDTO();
        sessionCodeDTO.setMobile("13207129596");
        sessionCodeDTO.setTimestamp(System.currentTimeMillis());
        sessionCodeDTO.setPersonalId("234458999");
        sessionCodeDTO.setMerId("18653");

        String data = JSON.toJSONString(sessionCodeDTO);
        System.out.println(data);
        //私钥1
        String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtfVX2zjJ9JPVZ8R6T6NFwZWmF/XC" +
                "pfwHWbTu+tnqVl2fFQODfPOBbNtvrPyWi4m09x9P6F8HEoY6+gq/Iko9ewIDAQABAkAfsssIZL9B" +
                "/VMLDb5lC0OGsuRJfkXXlq1NImkTiEz7mG2uyX2mdkSdmhBxs1+kMnYSOmbsPBDvPKHvz0Qp7hdh" +
                "AiEA+h8jjdn3iaX2SGAIlo84pqpWTwjO2etz9bLVE5nMtKsCIQC6PBlOlq0PhobXtIzTTonS+fev" +
                "INX/M6NGO4AdMH96cQIgb0zp+lZzA4qZhG1PhQfocqm7zGGkAm724++XR6iZ4g8CIQC4Jt1PXKbc" +
                "B0Ym3Z2zBKI8QHiub2Wr6D+3Hvbb5iznwQIhAN+GkKT4x+rq7PMNGPYcQFrAlMDu3gFttMKjXwr/" +
                "6Eif";
        String desKey = "mKGUDfeXxy8=";
        String developerId = "18653";


        String sign = SecurityUtil.signRSA(data, privateKey, SecurityUtil.CHARSET);

        String body = SecurityUtil.encryptDes(data, desKey, SecurityUtil.CHARSET);
        System.out.println("body"+body);
        System.out.println("sign"+sign);

        url = url.concat("merId=").concat(developerId).concat("&").concat("sign=").concat(URLEncoder.encode(sign)).concat("&").concat("body=").concat(URLEncoder.encode(body));
        System.out.println(url);

    }
}
