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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static String httpPostToJbfPay(String url, String data, String funcId, String privateKey, String developerId, String desKey) throws Exception {
        String rquestBody = httpRquestBodyToJbfPay(data, funcId, privateKey, developerId, desKey);
        String retXml = HttpClientUtil.httpPost(url, rquestBody, HttpClientUtil.CONTENT_TYPE);
        String responseBody = httpResponseBodyFromJbfPay(retXml, null,desKey, funcId);
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
     * @param retXml
     * @param desKey
     * @param funcId
     * @return
     * @throws Exception
     */
    public static String httpResponseBodyFromJbfPay(String retXml, String publicKey,String desKey, String funcId) throws Exception {
        DataDTO retDataDto = JAXB.unmarshal(new StringReader(retXml), DataDTO.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", retDataDto.getHeader().getRetCode());
        jsonObject.put("msg", retDataDto.getHeader().getRetMsg());
        String retBody = SecurityUtil.decryptDes(retDataDto.getBody(), desKey, "utf-8");
        if (!SecurityUtil.verifyRSA(retBody,publicKey,retDataDto.getHeader().getSignature(),SecurityUtil.CHARSET)) {
            jsonObject.put("msg", "验签失败");
        } else {
            if ("0000".equals(retDataDto.getHeader().getRetCode()) && StringUtils.isNotBlank(retDataDto.getBody())) {
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
                    case Constants.FUNC_ID_P7003:
                    case Constants.FUNC_ID_P7004:
                        jsonObject.put("data", retBody);
                        return  jsonObject.toJSONString();
                    default:
                        throw new Exception("错误的 funcId");
                }
                jsonObject.put("data", JAXB.unmarshal(new StringReader(retBody), classs));
            }
        }


        return jsonObject.toJSONString();
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void main(String[] args) throws Exception {
        //http://172.16.104.91/gatewayOnline//gateway/portal/execute http://116.7.255.40:53501/gatewayOnline/gateway/portal/execute
        String url = "http://localhost:8080/gateway/portal/execute";

        String data = "<data>" +
                "<personal_id>oKnuNxPKz3xaV2oeVVRc3kVcuDpM</personal_id>\n" +
                "<order_no>2019111410405026913295</order_no>\n" +
                "<merchant_no>1001003</merchant_no>\n" +
                "<goods_name>测试</goods_name>\n" +
                "<jump_url>www.baidu.com</jump_url>" +
                "<amt>0.01</amt>" +
                "<goods_id>测试</goods_id>" +
                "<client_type>4</client_type>" +
                "<notify_url>www.baidu.com</notify_url>" +
                "</data>";
        String privateKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAkOgxbFTlXfaWY4NNRTxS6FYNsV7qiV3GZ8KaHvfp48fQQLbcnhZj4j/+7kAlvjBcN8N/QmRis3W9sFQW7fJvoQIDAQABAkEAi2CKiSRvKZ3QsQ7N99Y5+Hcs3HnrJQ0plQu8qiTStt8zyELrfdEXT5k+A8oeqhtoyhzguoYRex0Gk2L46j1NAQIhAOxUy0DXV81sLh9rxLM2G1p1G+7gtvOtFR2kTEr7dwPpAiEAnPeKYpNormcv+/Mv8shOHIaCjAvxYpvyxZIPHy4XUvkCIDy3KCS3bkpLQao24Kj9/JcHwS5ksvv1ephL7oHRCumJAiEAk4he+3QNqg62nRrf8FOwh0MuJK98+/AT6Pr1V5sctRECIGyjqiIVHh74CyPPQiPC3H0SW3g1me5jvpg/CapO1VWi";
        String desKey = "hqHBE4o74Lo=";
        String developerId = "10735";
        String funcId = "P8001";
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIdvM8SIYUTiBrEGpT95jo7BV8YnTYz+dg2hjK9fWZVvKOB2EevKcgUL5Nt0uKkw9ObafzwNecnOkLiOCos/KX8CAwEAAQ==";




        String resp = httpRquestBodyToJbfPay(data,funcId,privateKey,developerId,desKey);


        //System.out.println(SecurityUtil.verifyRSA(data,publicKey,"O6UkLlHOHveU37Ab0ismkH/HZdq45jtcSSk3idtrV2poazSsAlVJhFM+DWgeVJWCFiJdtjKlssxLh+XEPVZjnA==","utf-8"));
        System.out.println(resp);
        resp= httpPostToJbfPay(url, data, funcId, privateKey, developerId, desKey);
        System.out.println(httpResponseBodyFromJbfPay(resp,publicKey,desKey,funcId));

    }
}
