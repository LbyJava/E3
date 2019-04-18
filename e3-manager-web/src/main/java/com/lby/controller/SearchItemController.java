package com.lby.controller;

import com.lby.common.utils.E3Result;
import com.lby.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/19 16:58
 */
@RestController
public class SearchItemController {
    @Autowired
    SearchItemService searchItemService;

    /**
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/index/item/import")
    public E3Result importItemList () throws Exception{
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }
}
