package com.lby.order.service;

import com.lby.common.utils.E3Result;
import com.lby.order.pojo.OrderInfo;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/26 16:39
 */
public interface OrderService {
    /**
     *
     * @param orderInfo
     * @return
     */
    E3Result createOrder(OrderInfo orderInfo);
}
