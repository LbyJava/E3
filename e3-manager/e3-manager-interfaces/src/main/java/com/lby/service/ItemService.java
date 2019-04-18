package com.lby.service;

import com.lby.common.pojo.EasyUIDataGridResult;
import com.lby.common.utils.E3Result;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbItemDesc;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/11 21:31
 */
public interface ItemService {
    /**
     * 根据Id查询商品
     *
     * @param id
     * @return
     */
    TbItem getTbItem(long id);

    /**
     * 查询商品
     *
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult getEasyUiDataGridResult(int page, int rows);

    /**
     * 添加商品条目
     * @param tbItem
     * @param desc
     * @return
     */
    E3Result addItem(TbItem tbItem,String desc);

    /**
     * 根据商品id查询商品描述
     * @param itemId
     * @return
     */
    TbItemDesc getTbItemDescById(long itemId);

    /**
     * 更新商品
     * @param tbItem
     * @param desc
     * @return
     */
    E3Result updateItem(TbItem tbItem, String desc);
}
