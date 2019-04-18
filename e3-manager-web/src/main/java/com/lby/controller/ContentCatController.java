package com.lby.controller;

import com.lby.common.pojo.EasyUITreeNode;
import com.lby.common.utils.E3Result;
import com.lby.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: TSF
 * @Description:内容分类管理
 * @Date: Create in 2018/12/17 17:16
 */
@RestController
public class ContentCatController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询分类
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/content/category/list")
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name = "id", defaultValue = "0") long parentId) throws Exception {
        List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
        return list;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    public E3Result createContentCategory(Long parentId,String name) throws Exception{
        E3Result e3Result = contentCategoryService.addContentCatgory(parentId, name);
        return e3Result;
    }
}
