package com.lby.content.service;

import com.lby.common.utils.E3Result;
import com.lby.pojo.TbContent;

import java.util.List;

/**
 * @Author: TSF
 * @Description:内容服务层
 * @Date: Create in 2018/12/18 0:26
 */
public interface ContentService {
    /**
     * 添加内容
     * @param tbContent
     * @return
     */
    E3Result addContent(TbContent tbContent);

    /**
     * 更具内容分类ID查询内容列表
     * @return
     */
    List<TbContent> getContentListByCid(long cid);
}
