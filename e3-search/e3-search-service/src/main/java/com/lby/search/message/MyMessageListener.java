package com.lby.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/21 15:45
 */
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("text = " + text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
