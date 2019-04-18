package com.lby.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/12 20:37
 */
@Controller
public class PageController {
    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/")
    public String showIndex() throws Exception{

        return "index";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) throws Exception {
        return page;
    }
    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/rest/page/item-edit")
    public String itemEdit() throws Exception {
        return "item-edit";
    }
}
