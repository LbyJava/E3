package com.lby.item.controller;

import com.lby.item.pojo.Item;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbItemDesc;
import com.lby.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/22 0:04
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) throws Exception{
        //调用服务取商品基本信息
        TbItem tbItem = itemService.getTbItem(itemId);
        Item item = new Item(tbItem);
        //取商品描述信息
        TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
        //把信息传递给页面
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        //返回逻辑视图
        return "item";
    }
}
