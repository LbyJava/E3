package com.lby.cart.interceptor;

import com.lby.common.utils.CookieUtils;
import com.lby.common.utils.E3Result;
import com.lby.pojo.TbUser;
import com.lby.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: TSF
 * @Description:用户登入处理拦截器
 * @Date: Create in 2018/12/24 19:42
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前处理，执行handler之前执行此方法。
        //返回true，放行false：拦截
        //1.从cookie中职token
        String token = CookieUtils.getCookieValue(request, "token");
        //2.如果没有token，未登录状态，直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //3.取到token，需要调用sso系统的服务，根据token取用户信息
        E3Result userByToken = tokenService.getUserByToken(token);
        //4.没有取到用户信息。登录过期，直接放行。
        if (userByToken.getStatus() != 200) {
            return true;
        }
        //5.取到用户信息。登录状态.
        TbUser user = (TbUser) userByToken.getData();
        //6.把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。
        request.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
