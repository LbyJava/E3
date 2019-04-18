package com.lby.cart.service;

import com.lby.common.utils.E3Result;
import com.lby.pojo.TbItem;

import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/24 23:04
 */
public interface CartService {
    /**
     * 用户登入后添加商品到购物车
     * @param userId
     * @param itemId
     * @return
     */
    E3Result addCart(long userId,long itemId,int num);

    /**
     * 合并购物车
     * @param userId
     * @param tbItems
     * @return
     */
    E3Result mergeCart(long userId, List<TbItem> tbItems);

    /**
     * 获取商品
     * @param userId
     * @return
     */
    List<TbItem> getCartList(long userId);

    /**
     * 更新商品
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    E3Result updateCart(long userId,long itemId,int num);

    /**
     * 删除商品
     * @param userId
     * @param itemId
     * @return
     */
    E3Result delectCart(long userId,long itemId);

    /**
     * 清空购物车
     * @param id 需要被清空购物车的用户id
     * @return の
     */
    E3Result clearCartItem(Long id);
}
