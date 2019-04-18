package com.lby.cart.service.impl;

import com.lby.cart.service.CartService;
import com.lby.common.jedis.JedisClient;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.mapper.TbItemMapper;
import com.lby.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/24 23:05
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Override
    public E3Result addCart(long userId, long itemId,int num) {
        //向redis中添加购物车

        //数据类型是hash key：用户id，field：商品id value:商品信息
        //判断商品是否存在
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            //把json转换成TbItem
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
            //写回redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //如果不存在，根据商品id取商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //设置购物车数量
        tbItem.setNum(num);
        //取一张图片
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        //添加到购物车列表
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        //返回成功
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> tbItems) {
        //遍历商品列表
        //把列表添加到购物车。
        //判断购物车中是否有此商品
        //如果有，数量相加
        //如果没有添加新的商品
        for (TbItem tbItem : tbItems) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> tbItems = new ArrayList<>();
        for (String s : jsonList) {
            //创建一个TbItem对象
            TbItem tbItem = JsonUtils.jsonToPojo(s, TbItem.class);
            //添加到列表
            tbItems.add(tbItem);
        }
        return tbItems;
    }

    @Override
    public E3Result updateCart(long userId, long itemId, int num) {
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result delectCart(long userId, long itemId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clearCartItem(Long id) {
        jedisClient.del(REDIS_CART_PRE + ":" + id);
        return E3Result.ok();
    }
}
