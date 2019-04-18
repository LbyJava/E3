package com.lby.controller;

import com.lby.common.pojo.EasyUIDataGridResult;
import com.lby.common.utils.E3Result;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbItemDesc;
import com.lby.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/11 20:55
 */
@RestController
public class ItemsController {

    @Autowired
    ItemService itemService;

    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/item/{id}")
    public TbItem selectItem(@PathVariable long id) throws Exception{
        TbItem tbItem = itemService.getTbItem(id);
        return tbItem;
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/item/list")
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) throws Exception {
        //调用服务查询商品列表
        EasyUIDataGridResult easyUiDataGridResult = itemService.getEasyUiDataGridResult(page, rows);
        return easyUiDataGridResult;
    }

    /**
     * 添加商品
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    public E3Result additem(TbItem tbItem, String desc) throws Exception {
        E3Result e3Result = itemService.addItem(tbItem, desc);
        return e3Result;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/rest/item/param/item/query/{itemId}")
    public E3Result getItemParam(@PathVariable Long itemId) throws Exception{
        TbItem tbItem = itemService.getTbItem(itemId);
        return E3Result.ok(tbItem);
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/rest/item/query/item/desc/{itemId}")
    public E3Result getItemDest(@PathVariable Long itemId) throws Exception{
        TbItemDesc tbItemDescById = itemService.getTbItemDescById(itemId);
        return E3Result.ok(tbItemDescById);
    }

    /**
     * 更新商品
     * @return
     * @throws Exception
     */
    @RequestMapping("/rest/item/update")
    public E3Result updateItem(TbItem tbItem,String desc) throws Exception{
        E3Result e3Result = itemService.updateItem(tbItem,desc);
        return e3Result;
    }
}
