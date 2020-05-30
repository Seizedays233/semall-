package com.seizedays.semall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.utils.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {
    public static void main(String[] args) {

        //1.请求申请授权
        final String CLIENT_ID = "394804488";
        final String REGISTERED_REDIRECT_URI = "http://127.0.0.1:9001/vlogin";

//        String doGet = "https://api.weibo.com/oauth2/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + REGISTERED_REDIRECT_URI;
//        String s = HttpClientUtil.doGet(doGet);
//        System.out.println(s);
//        System.out.println("url" + doGet);
        //2.获得授权码 http://访问地址?code=2487aebe3128add6156c50775a927999
        String s2 = "http://127.0.0.1:9001/vlogin?code=2487aebe3128add6156c50775a927999";

        //授权码存在有效期 授权码过期后需要重新授权 没生成一次授权码 说明用户对第三方数据进行重启授权，之前的授权码和token全部过期
        //用授权码获取到token后 token可长期使用
        String code = "5ad075395e696ba011da8169a766840a";

        //3.用授权码交换access_token令牌
        final String CLIENT_SECRET = "a02b1a49abc33a44579413fb5c6fd811";
//        String getAcce = "https://api.weibo.com/oauth2/access_token";
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("client_id", CLIENT_ID);
//        paramMap.put("client_secret", CLIENT_SECRET);
//        paramMap.put("grant_type","authorization_code");
//        paramMap.put("redirect_uri", REGISTERED_REDIRECT_URI);
//        paramMap.put("code", code);
//        System.out.println("accessURL " + getAcce);
//        String accessToken = HttpClientUtil.doPost(getAcce, paramMap);
//        System.out.println("accessToken" + accessToken);

        //accessTokenJson: {"access_token":"2.00IjpgII01gYi715147e14f10I1g1P","remind_in":"157679999","expires_in":157679999,"uid":"7457481962","isRealName":"true"}
        //token:  2.00IjpgII01gYi715147e14f10I1g1P
//        Map accessMap = JSON.parseObject(accessToken, Map.class);
//
//        System.out.println(accessMap.get("access_token"));


        //4. 使用access_token获取用户信息
        String token = "2.00IjpgII01gYi715147e14f10I1g1P";
        String uid = "7457481962";
        String userInfoURL = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid="+ uid;
        String userInfoJson = HttpClientUtil.doGet(userInfoURL);
        Map userMap = JSON.parseObject(userInfoJson, Map.class);
        System.out.println(userMap);

    }
}
