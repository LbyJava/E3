package com.lby.order.service.impl;

import com.lby.common.jedis.JedisClient;
import com.lby.common.utils.E3Result;
import com.lby.mapper.TbOrderItemMapper;
import com.lby.mapper.TbOrderMapper;
import com.lby.mapper.TbOrderShippingMapper;
import com.lby.order.pojo.OrderInfo;
import com.lby.order.service.OrderService;
import com.lby.pojo.TbOrder;
import com.lby.pojo.TbOrderItem;
import com.lby.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/26 17:01
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_START}")
    private String ORDER_ID_START;

    @Value("${ORDER_ID_GEN_EKY}")
    private String ORDER_ID_GEN;

    @Value("${ORDER_DETAIL_ID_GEN_KEY}")
    private String ORDER_DETAIL_ID_GEN_KEY;
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //生成订单号。使用redis的incr生成。
        if (!jedisClient.exists(ORDER_ID_GEN)) {
            jedisClient.set(ORDER_ID_GEN, ORDER_ID_START);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN).toString();
        //补全orderInfo的属性
        orderInfo.setOrderId(orderId);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderInfo);
        //向订单明细表插入数据。
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            //生成明细ID
            String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
            //补全POJO的属性
            orderItem.setId(odId);
            orderItem.setOrderId(orderId);
            //插入明细
            tbOrderItemMapper.insert(orderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        //向物流表插入数据
        tbOrderShippingMapper.insert(orderShipping);
        //返回のResult，包含订单号
        return E3Result.ok();
    }
}
