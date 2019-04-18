package com.lby.sso.controller;

import com.lby.common.utils.CookieUtils;
import com.lby.common.utils.E3Result;
import com.lby.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: TSF
 * @Description:登入处理
 * @Date: Create in 2018/12/23 17:16
 */
@Controller
public class LoginController {
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Autowired
    private LoginService loginService;

    /**
     * 登入页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model) throws Exception {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    /**
     * 登入
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        E3Result e3Result = loginService.userLogin(username, password);
        //判断是否登入成功
        if (e3Result.getStatus() == 200) {
            String token = e3Result.getData().toString();
            //如果登入成功需要把token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }
        //返回结果
        return e3Result;
    }
}
