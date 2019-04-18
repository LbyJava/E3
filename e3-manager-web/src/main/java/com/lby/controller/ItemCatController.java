package com.lby.controller;

import com.lby.common.pojo.EasyUITreeNode;
import com.lby.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/13 15:07
 */
@RestController
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/item/cat/list")
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0") long parentId) throws Exception{
        List<EasyUITreeNode> itemCatList = itemCatService.getItemCatList(parentId);
        return itemCatList;
    }
}
