package com.lby.content.service;

import com.lby.common.pojo.EasyUITreeNode;
import com.lby.common.utils.E3Result;

import java.util.List;

/**
 * @Author: TSF
 * @Description:内容分类管理
 * @Date: Create in 2018/12/17 16:58
 */
public interface ContentCategoryService {
    /**
     * 查询节点
     * @param parentID
     * @return
     */
    List<EasyUITreeNode> getContentCatList(long parentID);

    /**
     * 添加节点
     * @param parentId
     * @param name
     * @return
     */
    E3Result addContentCatgory(long parentId,String name);
}
