package com.lby.item.listener;

import com.lby.item.pojo.Item;
import com.lby.pojo.TbItem;
import com.lby.pojo.TbItemDesc;
import com.lby.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TSF
 * @Description:监听商品添加消息
 * @Date: Create in 2018/12/22 17:06
 */
public class HtmlGenListener implements MessageListener {
    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //创建模板
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品数据及描述
            TbItem tbItem = itemService.getTbItem(itemId);
            Item item = new Item(tbItem);
            //取商品描述
            TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
            //创建数据集，把商品数据封装
            Map data = new HashMap<>();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //加载模板对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流，指定输出的目录及文件名
            Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
            //生成静态页面
            template.process(data, out);
            //关闭流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
