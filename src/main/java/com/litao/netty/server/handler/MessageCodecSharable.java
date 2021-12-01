package com.litao.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.litao.netty.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;


@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> list) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1.起始字符'\n''\n'
        out.writeBytes(new byte[]{'\n','\n'});
        // 2.字符串长度
        JSON json = new JSONObject();
        String msg = json.toJSONString(message);
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
        int length = msgBytes.length;
        // 3.msg的字节数
        out.writeInt(length);
        // 4.msg的内容
        out.writeBytes(msgBytes);
        list.add(out);



    }

    @Override
    protected void decode(ChannelHandlerContext ch, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte b0 = byteBuf.readByte();
        byte b1 = byteBuf.readByte();
        if (b0 !=0x00 && b1 != 0x00){
            if (b0 == (char)'\n' && b1 ==(char)'\n'){
                int length = byteBuf.readInt();
                byte[] bytes = new byte[length];
                byteBuf.readBytes(bytes,0,length);
                // 把解析的字节
                String message = new String(bytes, "utf-8");

                // 接受到的数据格式
/*
                String Definedstr = {"TargetID":2,"SourceID":101,"MessageID":1,"MessageData":"{\"mode\":0.0,\"LinearVelocityX\":0.0,\"LinearVelocityY\":0.0,\"LinearVelocityZ\":0.0,\"Latitude\":0.0,\"Longitude\":0.0,\"Altitude\":1.0,\"SpatialPositionX\":0.0,\"SpatialPositionY\":0.0,\"SpatialPositionZ\":0.0}","TimeStamp":20957.9955}

 */
                logger.info("解析的内容："+message);
                list.add(message);

            }
        }

    }
}