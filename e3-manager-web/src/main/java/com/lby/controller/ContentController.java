package com.lby.controller;

import com.lby.common.utils.E3Result;
import com.lby.content.service.ContentService;
import com.lby.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: TSF
 * @Description:内容管理Controller
 * @Date: Create in 2018/12/18 0:33
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    public E3Result addContent(TbContent tbContent) throws Exception {
        //调用添加服务
        E3Result e3Result = contentService.addContent(tbContent);
        return e3Result;
    }
}
