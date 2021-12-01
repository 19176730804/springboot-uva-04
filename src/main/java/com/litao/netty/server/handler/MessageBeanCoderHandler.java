package com.litao.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.litao.config.BeanContext;
import com.litao.netty.message.Message;
import com.litao.netty.message.MessageData;
import com.litao.utils.RedisUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@ChannelHandler.Sharable
public class MessageBeanCoderHandler extends MessageToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void decode(ChannelHandlerContext ctx, Object o, List list) throws Exception {
        // 目前消息单一，直接转Message
        // 优化：设置一个分配机制，通过标识自动分配到javabean
        String msg = (String)o;
        // 解析string
        JSONObject parse = JSON.parseObject(msg);
        int targetID = (int) parse.get("TargetID");
        int sourceID = (int) parse.get("SourceID");
        int messageID = (int) parse.get("MessageID");
        MessageData messageData = (MessageData) parse.get("MessageData");

        Message message = new Message();
        message.setSourceID(sourceID);
        message.setTargetID(targetID);
        message.setMessageID(messageID);
        message.setMessageData(messageData);

        DoRedis doRedis = new DoRedis(message);
        Thread thread = new Thread(doRedis);
        thread.start();

        list.add(msg);
        // redis缓存存储数据


    }
      public class DoRedis implements Runnable{

        private Message msg;

        private DoRedis(Message msg){
            this.msg = msg;
        }

        @Override
        public void run() {
            RedisUtil bean = BeanContext.getApplicationContext().getBean(RedisUtil.class);
            Integer sourceInteger = msg.getSourceID();
            String sourceID = String.valueOf(sourceInteger);
            bean.set("UVA" + sourceID, msg);
            Message message = (Message)bean.get("UVA" + sourceID);
            logger.info("redis保存的数据："+ message);
        }
    }

}
