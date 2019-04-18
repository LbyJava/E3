package com.lby.cart.controller;

import com.lby.cart.service.CartService;
import com.lby.common.utils.CookieUtils;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbUser;
import com.lby.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TSF
 * @Description:购物车处理Controller
 * @Date: Create in 2018/12/24 15:14
 */
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登入状态，把购物车写入redis
        if (user != null) {
            //保存到服务端
            cartService.addCart(user.getId(), itemId, num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //如果未登入使用cookie
        //从Cookie中取购物车列表
        List<TbItem> cartItemFromCookie = getCartItemFromCookie(request);
        // 判断商品列表中是否存在
        boolean flag = false;
        for (TbItem tbItem : cartItemFromCookie) {
            //如果存在数量相加
            if (tbItem.getId() == itemId.longValue()) {
                flag = true;
                //找到商品，数量相加
                tbItem.setNum(tbItem.getNum() + num);
                //跳出循环
                break;
            }
        }
        //如果不存在，根据商品id查询商品信息。得到TbItem
        if (!flag) {
            TbItem tbItem = itemService.getTbItem(itemId);
            //设置商品数量
            tbItem.setNum(num);
            //取一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                tbItem.setImage(image.split(",")[0]);
            }
            // 把商品添加到商品列表
            cartItemFromCookie.add(tbItem);
        }
        //写入Cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartItemFromCookie), COOKIE_CART_EXPIRE, true);
        //返回成功页面
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表的处理
     *
     * @param request
     * @return
     */
    private List<TbItem> getCartItemFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断json是否为空
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
        return tbItems;
    }

    @RequestMapping("/cart/cart")
    public String showCatList(HttpServletRequest request,HttpServletResponse response) {
        //从cookie中取购物车列表
        List<TbItem> cartItemFromCookie = getCartItemFromCookie(request);
        //判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登入状态，从cookie中去购物车列表
        if (user != null) {
            //如果不为空，把cookie中的购物车商品和服务的购物车合并。
            cartService.mergeCart(user.getId(), cartItemFromCookie);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request, response, "cart");
            //从服务端取购物车列表
            cartItemFromCookie = cartService.getCartList(user.getId());
        }

        //如果未登入使用cookie
        //把列表传递给页面
        request.setAttribute("cartList", cartItemFromCookie);
        //返回逻辑视图
        return "cart";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登入状态，把购物车写入redis
        if (user != null) {
            //保存到服务端
            E3Result e3Result = cartService.updateCart(user.getId(), itemId, num);
            //返回逻辑视图
            return e3Result;
        }

        //从cookie中取购物车列表
        List<TbItem> cartItemFromCookie = getCartItemFromCookie(request);
        //遍历商品找到对应的商品
        for (TbItem tbItem : cartItemFromCookie) {
            //如果存在数量相加
            if (tbItem.getId() == itemId.longValue()) {
                //找到商品，更新数量
                tbItem.setNum(num);
                //跳出循环
                break;
            }
        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartItemFromCookie), COOKIE_CART_EXPIRE, true);
        //返回成功
        return E3Result.ok();
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登入状态，把购物车写入redis
        if (user != null) {
            //保存到服务端
            E3Result e3Result = cartService.delectCart(user.getId(), itemId);
            //返回逻辑视图
            return "redirect:/cart/cart.html";
        }
        //从cookie中取出购物车列表
        List<TbItem> cartItemFromCookie = getCartItemFromCookie(request);
        //遍历列表，找到要删除的商品
        for (TbItem tbItem : cartItemFromCookie) {
            //如果存在数量相加
            if (tbItem.getId() == itemId.longValue()) {
                //删除商品
                cartItemFromCookie.remove(tbItem);
                //跳出循环
                break;
            }

        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartItemFromCookie), COOKIE_CART_EXPIRE, true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }
}