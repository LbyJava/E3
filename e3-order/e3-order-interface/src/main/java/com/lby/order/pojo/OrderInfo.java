package com.lby.order.pojo;

import com.lby.pojo.TbOrder;
import com.lby.pojo.TbOrderItem;
import com.lby.pojo.TbOrderShipping;

import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/26 15:38
 */
public class OrderInfo extends TbOrder {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
