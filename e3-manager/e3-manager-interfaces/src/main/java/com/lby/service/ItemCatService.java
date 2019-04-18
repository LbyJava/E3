package com.lby.service;

import com.lby.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/13 14:41
 */
public interface ItemCatService {

    /**
     * 查询节点
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getItemCatList(long parentId);
}
