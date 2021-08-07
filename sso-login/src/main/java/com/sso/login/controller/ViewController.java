package com.sso.login.controller;

import com.sso.login.pojo.User;
import com.sso.login.utils.LoginCacheUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 页面跳转逻辑
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String toLogin(@RequestParam(required = false,defaultValue = "") String target,
                          HttpSession session, @CookieValue(required = false,value = "TOKEN") Cookie cookie) {

        if (StringUtils.isEmpty(target)) {
            target = "http://www.codeshop.com:9010";
        }
        //如果是已经登录的用户再次访问登录系统，重定向
        if (cookie != null) {
            String value = cookie.getValue();
            User user = LoginCacheUtil.loginUser.get(value);
            if (user != null) {
                return "redirect:" + target;
            }
        }

        //TODO:对target进行校验
        //重定向地址
        session.setAttribute("target",target);
        return "login";
    }

    @GetMapping("/logout")
    public String toLogout(@RequestParam(required = false,defaultValue = "") String target,
                           @CookieValue(required = false,value = "TOKEN")Cookie cookie,
                           HttpServletResponse response) {
        if (StringUtils.isEmpty(target)) {
            target = "http://www.codeshop.com:9010";
        }

        if(cookie != null) {
            cookie.setMaxAge(0);
        }

        LoginCacheUtil.loginUser.remove(cookie.getValue());
        return "redirect:" + target;
    }
}
