package com.lby.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lby.common.jedis.JedisClient;
import com.lby.common.pojo.EasyUIDataGridResult;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.IDUtils;
import com.lby.common.utils.JsonUtils;
import com.lby.mapper.TbItemDescMapper;
import com.lby.mapper.TbItemMapper;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbItemDesc;
import com.lby.pojo.TbItemExample;
import com.lby.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/11 21:02
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestination")
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CHCHE_EXPIRE}")
    private Integer ITEM_CHCHE_EXPIRE;

    @Override
    public TbItem getTbItem(long id) {
        //添加缓存
        try {
            //查询缓存
            String s = jedisClient.get(REDIS_ITEM_PRE + ":" + id + ":BASE");
            if (StringUtils.isNotBlank(s)) {
                TbItem tbItem = JsonUtils.jsonToPojo(s, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        //把结果添加到缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + id + ":BASE", JsonUtils.objectToJson(tbItem));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", ITEM_CHCHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }

    @Override
    public EasyUIDataGridResult getEasyUiDataGridResult(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        //创建一个返回值对象
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setRows(tbItems);
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        //取记录总数
        long total = pageInfo.getTotal();
        easyUIDataGridResult.setTotal(total);
        //取结果
        return easyUIDataGridResult;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全商品属性
        tbItem.setId(itemId);
        //设置商品状态
        //1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //插入商品
        tbItemMapper.insert(tbItem);
        //创建商品对应的商品详情对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //补全商品详情对象
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //插入商品详情
        tbItemDescMapper.insert(tbItemDesc);
        //发送一个商品添加消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });
        //返回成功
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getTbItemDescById(long itemId) {
        //添加缓存
        try {
            //查询缓存
            String s = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(s)) {
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(s, TbItemDesc.class);
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //把结果添加到缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CHCHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }

    @Override
    public E3Result updateItem(final TbItem tbItem, String desc) {
        //修改更新时间
        tbItem.setUpdated(new Date());
        //修改商品
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
        //创建商品对应的商品详情对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //补全商品详情对象
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(new Date());
        //修改商品详情
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        //发送一个商品修改消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(tbItem.getId() + "");
                return textMessage;
            }
        });
        jedisClient.del(REDIS_ITEM_PRE + ":" + tbItem.getId() + ":DESC");
        //返回成功
        return E3Result.ok();
    }
}
