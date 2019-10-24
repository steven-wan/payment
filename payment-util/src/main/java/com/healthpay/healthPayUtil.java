package com.healthpay;

import com.alibaba.fastjson.JSONObject;
import com.healthpay.dto.apidto.*;
import com.healthpay.dto.httpbodydto.DataDTO;
import com.healthpay.dto.httpbodydto.HeaderDTO;
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
        data = replaceBlank(data);
        DataDTO dataDTO = new DataDTO();
        //header赋值
        HeaderDTO headerDTO = new HeaderDTO();
//        headerDTO.setRetCode("0000");
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
        String retBody = SecurityUtil.decryptDes(retDataDto.getBody(), desKey, "utf-8");
        if (!SecurityUtil.verifyRSA(retBody, publicKey, retDataDto.getHeader().getSignature(), SecurityUtil.CHARSET)) {
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
        String url = "http://172.16.104.91/gatewayOnline//gateway/portal/execute";

        String data = "<data>" +
                "<personal_id>0014357701cc461f9229a3171667164d</personal_id>" +
                "<withdraw_amt>200</withdraw_amt>" +
                "</data>";
        String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmPWKRtadMBWyOJfRYv4Tio6ofwUvuSqkHlo0L6ybxtTomyyA3DAK4AqLT/68wV++Iq1zHplr/o71Ne3GcGj1sQIDAQABAkBm75cG5RcTbQZrH4BB5kqwTCEImizrp86avomwK7EJtOJbNQH/MX1/vSTHivsCWRRMPbI7jwKlk1mGxiF0FX+BAiEA3fqxZRzrftOwWja0oWoWnQCCPwSQowAJKgnWXDHlb3UCIQCwZtueNQIpyH6SNw5gLJZoAojTpcCKpqGpB5hRtppBzQIgaTkewgl1cEc0f28TLLi3Q3EsTGcDkODBd1X2cPmOAskCIQCNS3YyPOdkrHGlkG/1XQGEgjMyxBt7c4kdqTW6jfb1KQIgNJBpN1c9ErOIz7wIAZ0v4WDlvYZwNxXBKCqMB8jB/SQ=";
        String desKey = "IIAa1quPRpg=";
        String developerId = "59027";
        String funcId = "R1001";
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJj1ikbWnTAVsjiX0WL+E4qOqH8FL7kqpB5aNC+sm8bU6JssgNwwCuAKi0/+vMFfviKtcx6Za/6O9TXtxnBo9bECAwEAAQ==";



//        String retj = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<root>\n" +
//                "    <head>\n" +
//                "        <developer_id>59027</developer_id>\n" +
//                "        <func_id>F1006</func_id>\n" +
//                "        <timestamp>1571035236912</timestamp>\n" +
//                "        <signature>\n" +
//                "            <![CDATA[lIyjI6JY0CkFtMJke/qiYqxg6YNiNuYGHkfpjaaIBPNGv1z0lNvc3JsQVL5zb8cU1A5jTACgGRaSNNZGLinmUg==]]>\n" +
//                "        </signature>\n" +
//                "        <ret_code>0000</ret_code>\n" +
//                "        <ret_msg></ret_msg>\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "        <![CDATA[k3aXMKDeRbDDa67rj5fGeY+7Rp1o1gVlyfCT79QDQZVnjy2fVHFQj3hB0hnPvWBjxDLYrv113Up9X/14WtsS6gXBbSBQL2TGdrICv7EogLCleXkQoK5PvFMiZHgG5QL7wQ4ckP7+OQd4ICcGW/YIazcI7jidJrxikdWkLuloCPiKtM2XGJFxFw4E/xXXl+Yw]]>\n" +
//                "    </body>\n" +
//                "</root>";
//
//        System.out.println("healthPayUtil.httpResponseBodyFromJbfPay(retj,publicKey,desKey,funcId) = " + healthPayUtil.httpResponseBodyFromJbfPay(retj, publicKey, desKey, funcId));

        String resp = healthPayUtil.httpRquestBodyToJbfPay(data, funcId, privateKey, developerId, desKey);
        System.out.println("resp = " + resp);
        System.out.println("replaceBlank() = " + httpResponseBodyFromJbfPay(resp, publicKey, desKey,funcId ));
        // System.out.println(healthPayUtil.httpResponseBodyFromJbfPay(retj,desKey ,funcId));
//        JSONObject jsonObject = new JSONObject();
//        P2005RetDTO p2005RetDTO = new P2005RetDTO();
//        p2005RetDTO.setOrderNo("ddd");
//        jsonObject.put("code","0000");
//        jsonObject.put("data",p2005RetDTO);
//        System.out.println("jsonObject.toJSONString() = " + jsonObject.toJSONString());
    }
}
