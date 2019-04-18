package com.lby.portal.controller;

import com.lby.content.service.ContentService;
import com.lby.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/17 15:14
 */
@Controller
public class IndexController {
    @Autowired
    private ContentService contentService;

    @Value("${index.carousel}")
    private Long CONTENT_LUNBO_ID;


    /**
     * 主页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/index")
    public String showIndex(Model model) throws Exception {
        //查询内容列表
        List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
        //把结果传递给页面
        model.addAttribute("ad1List", ad1List);
        return "index";
    }
}
