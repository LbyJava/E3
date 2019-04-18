package com.lby.content.service.impl;

import com.lby.common.jedis.JedisClient;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.content.service.ContentService;
import com.lby.mapper.TbContentMapper;
import com.lby.pojo.TbContent;
import com.lby.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/18 0:27
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent tbContent) {
        //将属性字段补全
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        //插入到数据库
        tbContentMapper.insert(tbContent);
        //缓存同步，删除缓存中对应的数据
        jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //查询缓存
        try {
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> tbContents = JsonUtils.jsonToList(json, TbContent.class);
                return tbContents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(contentExample);
        //添加缓存
        try {
            jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(tbContents));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }
}
