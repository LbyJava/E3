package com.lby.content.service.impl;
import java.util.Date;

import com.lby.common.pojo.EasyUITreeNode;
import com.lby.common.utils.E3Result;
import com.lby.content.service.ContentCategoryService;
import com.lby.mapper.TbContentCategoryMapper;
import com.lby.pojo.TbContentCategory;
import com.lby.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/17 17:00
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentID) {
        //根据parentId查询字节点列表
        TbContentCategoryExample categoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = categoryExample.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentID);
        //执行查询
        List<TbContentCategory> categoryList = tbContentCategoryMapper.selectByExample(categoryExample);
        //转换成EasyUITreeNode的列表
        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
        for (TbContentCategory tbContentCategory : categoryList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            //添加到列表
            easyUITreeNodes.add(node);
        }
        return easyUITreeNodes;
    }

    @Override
    public E3Result addContentCatgory(long parentId, String name) {
        //创建一个tb_content_category表对应的pojo对象
        TbContentCategory category = new TbContentCategory();
        //设置pojo的属性
        category.setParentId(parentId);
        category.setName(name);
        //状态1-正常2-删除
        category.setStatus(1);
        //默认排序是1
        category.setSortOrder(1);
        //新添加的节点一定是叶子节点
        category.setIsParent(false);
        category.setCreated(new Date());
        category.setUpdated(new Date());
        //插入到数据库
        tbContentCategoryMapper.insert(category);
        //判断父节点isParent属性，如果不是true改为true
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果，返回のresult
        return E3Result.ok(category);
    }
}
