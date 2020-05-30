package com.seizedays.semall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.passport.services.PassPortService;
import com.seizedays.semall.utils.HttpClientUtil;
import com.seizedays.semall.webutils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassPortController {

    @Resource
    PassPortService passPortService;

    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request) {

        String token = "";

        //调用用户认证服务验证用户名和密码
        UmsMember umsMemberLogin = passPortService.login(umsMember);
        if (null != umsMemberLogin) {
            //登录成功

            //用jwt制作token
            //实际开发需要对key"2020semall0516"和ip进行md5加密后再传入encode方法
            //JwtUtil算法也需要进行手动设计
            String key = "2020semall0516";
            Long memberId = umsMemberLogin.getId();
            String nickname = umsMemberLogin.getNickname();
            String ip = request.getHeader("x-forwarded-for"); //通过Nginx转发的客户端ip
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr(); //从request获取ip
            }

            if (StringUtils.isBlank(ip)) {
                //如果从Nginx和request中获得的ip都为空 进行异常处理 此处暂时将ip赋值为127.0.0.1
                ip = "127.0.0.1";
            }
            token = makTokenByJwt(key, memberId.toString(), nickname, ip);

            //将token存入redis一份

            passPortService.addUserToken(token, memberId);

        } else {
            //登录失败
            token = "fail";
        }

        return token;
    }

    @RequestMapping("/index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        if (StringUtils.isNotBlank(ReturnUrl)) {
            modelMap.put("ReturnUrl", ReturnUrl);
        }
        return "index";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token, String currentIp) {
        //通过jwt校验token真假
        Map<String, String> map = new HashMap<>();


        Map<String, Object> decode = JwtUtil.decode(token, "2020semall0516", currentIp);
        if (null != decode) {
            map.put("status", "success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickName", (String) decode.get("nickName"));
        } else {
            map.put("status", "fail");
        }

        return JSON.toJSONString(map);
    }

    //新浪微博账号登录
    @RequestMapping("/vlogin")
    public String vlogin(String code, HttpServletRequest request) {
        //授权码换取access_token
        final String CLIENT_ID = "394804488";
        final String CLIENT_SECRET = "a02b1a49abc33a44579413fb5c6fd811";
        final String REGISTERED_REDIRECT_URI = "http://127.0.0.1:9001/vlogin";

        String getAcce = "https://api.weibo.com/oauth2/access_token";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", CLIENT_ID);
        paramMap.put("client_secret", CLIENT_SECRET);
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", REGISTERED_REDIRECT_URI);
        paramMap.put("code", code);
        String accessTokenJson = HttpClientUtil.doPost(getAcce, paramMap);
        Map<String, String> accessMap = JSON.parseObject(accessTokenJson, Map.class);
        String token = accessMap.get("access_token");
        System.out.println("accessToken" + token);

        //access_token换取用户信息
        String uid = accessMap.get("uid");
        String userInfoURL = "https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + uid;
        String userInfoJson = HttpClientUtil.doGet(userInfoURL);
        Map<String, Object> userMap = JSON.parseObject(userInfoJson, Map.class);
        System.out.println(userMap);
        String nickName = (String) userMap.get("screen_name");
        //将用户信息保存到数据库，用户类型设置为微博用户
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType(2);
        umsMember.setAccessToken(token);
        umsMember.setAccessCode(code);
        umsMember.setSourceUid((Long) userMap.get("id"));
        umsMember.setCity((String) userMap.get("location"));
        umsMember.setNickname(nickName);
        String gender = (String) userMap.get("gender");
        if (gender.equals("m")) {
            umsMember.setGender(1);
        } else if (gender.equals("f")) {
            umsMember.setGender(2);
        } else {
            umsMember.setGender(0);
        }

        passPortService.addOauthUser(umsMember);

        //生成jwt的token 并且携带该token重定向到首页
        String key = "2020semall0516";
        String ip = request.getHeader("x-forwarded-for"); //通过Nginx转发的客户端ip
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr(); //从request获取ip
        }

        if (StringUtils.isBlank(ip)) {
            //如果从Nginx和request中获得的ip都为空 进行异常处理 此处暂时将ip赋值为127.0.0.1
            ip = "127.0.0.1";
        }

        String tokenV = makTokenByJwt(key, uid, nickName, ip);

        //将token存入redis
        passPortService.addUserToken(tokenV, Long.valueOf(uid));

        return "redirect:http://localhost:9022/index?token=" + tokenV;
    }

    private String makTokenByJwt(String key, String id, String name, String ip) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("memberId", id);
        userMap.put("nickName", name);


        //实际开发需要对key"2020semall0516"和ip进行md5加密后再传入encode方法
        //JwtUtil算法也需要进行手动设计
        return JwtUtil.encode(key, userMap, ip);
    }

}
