package com.lby.service.impl;

import com.lby.common.pojo.EasyUITreeNode;
import com.lby.mapper.TbItemCatMapper;
import com.lby.pojo.TbItemCat;
import com.lby.pojo.TbItemCatExample;
import com.lby.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/13 14:43
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据节点父节点查列表
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);
        //转类型
        List<EasyUITreeNode> resultList = new ArrayList<>();
        //把列表转换成EasyUITreeNode列表
        for (TbItemCat tbItemCat : tbItemCats) {
            EasyUITreeNode node = new EasyUITreeNode();
            //设置属性
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            //添加到集合列表
            resultList.add(node);
        }
        return resultList;
    }
}
