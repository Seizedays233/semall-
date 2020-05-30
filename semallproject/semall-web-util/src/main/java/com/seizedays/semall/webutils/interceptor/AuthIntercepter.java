package com.seizedays.semall.webutils.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.filter.IFilterConfig;
import com.seizedays.semall.utils.HttpClientUtil;
import com.seizedays.semall.webutils.CookieUtil;
import com.seizedays.semall.annotations.LoginRequired;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //拦截代码

        //判断被拦截请求的访问方法的注解是否是需要拦截的
        //通过反射得到这个方法的注解
        StringBuffer requestURL1 = request.getRequestURL();
        System.out.println(requestURL1);
        LoginRequired methodAnnotation = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
        } else {
            return true;
        }

        if (null == methodAnnotation) {
            //没有注解 不进行拦截处理 直接返回true
            return true;
        }

        //被拦截 需要对token进行验证
        // oldToken newToken均为空 说明从未登录过 old不为空 newToken为空说明已登录
        // oldToken为空 newToken不为空 说明刚刚从认证中心登录
        // old和new均不为空 说明oldToken已过期 新token刚刚完成登录
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);


        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }

        String newToken = request.getParameter("token");

        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }

        //用于判断是否必须登录才能执行
        boolean loginSuccessRequired = methodAnnotation.loginSuccessRequired();

        String success = "";
        Map<String, String> successMap = new HashMap<>();
        String ip = request.getHeader("x-forwarded-for"); //通过Nginx转发的客户端ip
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr(); //从request获取ip
        }

        if (StringUtils.isBlank(ip)) {
            //如果从Nginx和request中获得的ip都为空 进行异常处理 此处暂时将ip赋值为127.0.0.1
            ip = "127.0.0.1";
        }
        //调用认证中心进行验证
        if (StringUtils.isNotBlank(token)) {
            String successJson = HttpClientUtil.doGet("http://localhost:9001/verify?token=" + token + "&currentIp=" + ip);

            successMap = JSON.parseObject(successJson, Map.class);

            success = successMap.get("status");

        }
        if (loginSuccessRequired) {
            //必须登录成功才能使用
            if (!"success".equals(success)) {
                //重定向回登录页面
                StringBuffer requestURL = requestURL1;
                response.sendRedirect("http://localhost:9001/index?ReturnUrl=" + requestURL);
                return false;
            }

            //验证通过
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickName", successMap.get("nickName"));
            //覆盖cookie中的token
            if (StringUtils.isNotBlank(token)) {
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
            }


        } else {
            //不需要登录也能使用但是必须验证（购物车）
            //验证
            if ("success".equals(success)) {
                //成功验证 将token携带的用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickName", successMap.get("nickName"));
                //覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                }
            }

        }

        return true;

    }

}
