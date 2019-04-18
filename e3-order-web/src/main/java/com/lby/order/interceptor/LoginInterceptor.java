package com.lby.order.interceptor;

import com.lby.cart.service.CartService;
import com.lby.common.utils.CookieUtils;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbUser;
import com.lby.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/25 23:31
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //2.判断token是否存在
        if (StringUtils.isBlank(token)) {
            //如果token不存在，为登录状态，跳转到sso系统的登入页面。用户登入后，跳转到当前请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //3.取到token，需要调用sso系统的服务，根据token取用户信息
        E3Result userByToken = tokenService.getUserByToken(token);
        //4.没有取到用户信息。登录过期，直接放行。
        if (userByToken.getStatus() != 200) {
            //如果token过期，为登录状态，跳转到sso系统的登入页面。用户登入后，跳转到当前请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //5.取到用户信息。登录状态.
        TbUser user = (TbUser) userByToken.getData();
        //6.把用户信息放到request中。
        request.setAttribute("user", user);
        //判断cookie中是否有购物车数据，如果有就合并到服务端
        String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(jsonCartList)) {
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
