package com.healthpay;

import com.alibaba.fastjson.JSONObject;
import com.healthpay.dto.apidto.*;
import com.healthpay.dto.httpbodydto.DataDTO;
import com.healthpay.dto.httpbodydto.HeaderDTO;
import com.healthpay.httpUtil.HttpClientUtil;
import com.healthpay.securityUtil.SecurityUtil;
import com.healthpay.dto.*;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;
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
        pay();
    }

    public static void callBack() throws Exception {

        //公钥1
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKxhFYJOUdRp0wY7QfntcJ3lVZ4G1hEMWNSSLGezzS9L8OV/NFy57AyhcHKYnLqw2ZSURo4xLHSNFjc4ASsi/PMCAwEAAQ==";

        String privateKey="MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtfVX2zjJ9JPVZ8R6T6NFwZWmF/XC\n" +
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
        bodyxml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
        System.out.println(httpResponseBodyFromJbfPay(bodyxml , publicKey, desKey, funcId));
    }

    public static void pay() throws Exception {
        //http://172.16.104.91/gatewayOnline//gateway/portal/execute http://116.7.255.40:53501/gatewayOnline/gateway/portal/execute
        String url = "http://dev.jbf.aijk.net/gatewayOnline/gateway/portal/execute";

        String data = "<data>" +
                "<personal_id>oKnuNxPKz3xaV2oeVVRc3kVcuDpM</personal_id>\n" +
                "<order_no>2020111410405026913291</order_no>\n" +
                "<merchant_no>1001003</merchant_no>\n" +
                "<goods_name>测试</goods_name>\n" +
                "<jump_url>www.baidu.com</jump_url>" +
                "<amt>0.01</amt>" +
                "<goods_id>测试</goods_id>" +
                "<client_type>4</client_type>" +
                "<notify_url>www.baidu.com</notify_url>" +
                "</data>";
        //私钥1
        String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAkOgxbFTlXfaWY4NNRTxS6FYNsV7qiV3GZ8KaHvfp48fQQLbcnhZj4j/+7kAlvjBcN8N/QmRis3W9sFQW7fJvoQIDAQABAkEAi2CKiSRvKZ3QsQ7N99Y5+Hcs3HnrJQ0plQu8qiTStt8zyELrfdEXT5k+A8oeqhtoyhzguoYRex0Gk2L46j1NAQIhAOxUy0DXV81sLh9rxLM2G1p1G+7gtvOtFR2kTEr7dwPpAiEAnPeKYpNormcv+/Mv8shOHIaCjAvxYpvyxZIPHy4XUvkCIDy3KCS3bkpLQao24Kj9/JcHwS5ksvv1ephL7oHRCumJAiEAk4he+3QNqg62nRrf8FOwh0MuJK98+/AT6Pr1V5sctRECIGyjqiIVHh74CyPPQiPC3H0SW3g1me5jvpg/CapO1VWi";
        String desKey = "hqHBE4o74Lo=";
        String developerId = "10735";
        String funcId = "P8001";

        //公钥1
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJDoMWxU5V32lmODTUU8UuhWDbFe6oldxmfCmh736ePH0EC23J4WY+I//u5AJb4wXDfDf0JkYrN1vbBUFu3yb6ECAwEAAQ==";


        System.out.println("time1:"+System.currentTimeMillis());
        String resp = httpRquestBodyToJbfPay(data, funcId, privateKey, developerId, desKey);

        System.out.println("time2:"+System.currentTimeMillis());
        //System.out.println(SecurityUtil.verifyRSA(data,publicKey,"O6UkLlHOHveU37Ab0ismkH/HZdq45jtcSSk3idtrV2poazSsAlVJhFM+DWgeVJWCFiJdtjKlssxLh+XEPVZjnA==","utf-8"));
        System.out.println(resp);
        //公钥2
        publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIdvM8SIYUTiBrEGpT95jo7BV8YnTYz+dg2hjK9fWZVvKOB2EevKcgUL5Nt0uKkw9ObafzwNecnOkLiOCos/KX8CAwEAAQ==";
        System.out.println("time3:"+System.currentTimeMillis());
        resp = httpPostToJbfPay(url, data, funcId, privateKey, developerId, desKey, publicKey);
        //System.out.println(httpResponseBodyFromJbfPay(resp, publicKey, desKey, funcId));
        System.out.println("time4:"+System.currentTimeMillis());
        System.out.println(resp);
    }
}
