package com.litao.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.litao.netty.message.Message;
import com.litao.netty.session.SessionFactory;
import com.litao.utils.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ChannelHandler.Sharable
public class LoginMessageHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        // 解析string
        JSONObject parse = JSON.parseObject(message);
        int targetID = (int) parse.get("TargetID");
        String targetIDString = Integer.toString(targetID);
        int sourceID = (int) parse.get("SourceID");
        String sourceIDString = Integer.toString(sourceID);
        int messageID = (int) parse.get("MessageID");


        /*Integer sourceID = message.getSourceID();
        String sourceIDString = Integer.toString(sourceID);
        Integer targetID = message.getTargetID();
        String targetIDString = Integer.toString(targetID);*/

        if ("1".equals(messageID)){
            List<Channel> allChannel = SessionFactory.getSession().getAllChannel();
            for (Channel ch : allChannel){
                if (ch != SessionFactory.getSession().getChannel(sourceIDString)){
                    ch.writeAndFlush(message);
                }
            }
        }else {
            Channel channel1 = SessionFactory.getSession().getChannel(sourceIDString);
            Channel channel2 = SessionFactory.getSession().getChannel(targetIDString);
            if (channel1 != null){
                //System.out.println(sourceIDString +"已经注册");
                if ( channel2 == null){
                    //channel1.writeAndFlush(target +"downline");
                    //System.out.println(targetIDString +"downline");
                }else {
                    System.out.println(sourceIDString + "已经发送消息");
                    channel2.writeAndFlush(message);
                }
            }else {
                SessionFactory.getSession().bind(ctx.channel(), sourceIDString);
                String res = "success to bind";
                //System.out.println(res);
                if (channel2 != null){
                    System.out.println("bind to send:"+message);
                    channel2.writeAndFlush(message);
                }
            }
        }

    }
}
