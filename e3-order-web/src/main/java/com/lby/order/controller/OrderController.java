package com.lby.order.controller;

import com.lby.cart.service.CartService;
import com.lby.common.utils.E3Result;
import com.lby.order.pojo.OrderInfo;
import com.lby.order.service.OrderService;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/25 22:58
 */
@Controller
public class OrderController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request) throws Exception {
        //取用户id
        TbUser user = (TbUser) request.getAttribute("user");
        //通常订单查询还有
        //取用户收货地址
        //取支付方式列表
        //根据用户id取购物车列表
        List<TbItem> cartList = cartService.getCartList(user.getId());
        //把购物车传递给页面
        request.setAttribute("cartList", cartList);
        //返回逻辑视图
        return "order-cart";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) throws Exception {
        //取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //把用户信息添加到orderInfo中
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用服务生成订单
        E3Result order = orderService.createOrder(orderInfo);
        //如果订单生成成功，删除购物车
        if (order.getStatus() == 200) {
            cartService.clearCartItem(user.getId());
        }
        //把订单号传递给页面
        request.setAttribute("orderId", order.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        //返回逻辑视图
        return "success";
    }
}
