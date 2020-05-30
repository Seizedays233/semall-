package com.seizedays.semall.webutils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJWT {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberID", "32");
        map.put("nickName", "zhangsan");
        String ip = "127.0.0.1";
        String time = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss").format(new Date());
        String encode = JwtUtil.encode("2020semall0516", map, ip + time);

        System.err.println(encode);

        //jwt加密的token是可以被base64解析出来的 只有签名的盐值是安全的
        String tokenUserInfo = StringUtils.substringBetween(encode,".");
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] tokenBytes = base64UrlCodec.decode(tokenUserInfo);
        String tokenJson = null;

        try {
            tokenJson = new String(tokenBytes, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        Map map1 = JSON.parseObject(tokenJson, Map.class);

        System.out.println("64=" + map1);
    }
}
