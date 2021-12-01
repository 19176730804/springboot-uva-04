package com.litao.netty.server.handler;

import com.litao.netty.protocol.ProcotolFrameDecoder;
import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.springframework.stereotype.Component;


/**
 * ChannelInitializer，它用于 Channel 创建时，实现自定义的初始化逻辑。
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {


    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
    MessageBeanCoderHandler MESSAGE_BEAN = new MessageBeanCoderHandler();
    LoginMessageHandler LOGGINF_REGIST = new LoginMessageHandler();
    AdaptChannelHandler ADAPT_HANDLER = new AdaptChannelHandler();

    /**
     * 在每一个客户端与服务端建立完成连接时，服务端会创建一个 Channel 与之对应。
     * 此时，NettyServerHandlerInitializer 会进行执行 #initChannel(Channel c) 方法，进行自定义的初始化。
     *
     * @param channel
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {


        // 获得 Channel 对应的 ChannelPipeline
        // 获得客户端 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = channel.pipeline();
        // 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        channelPipeline
                .addLast(new ProcotolFrameDecoder())
                // 编码器、解码器
                .addLast(MESSAGE_CODEC)
                // bean转换器
                .addLast(MESSAGE_BEAN)
                // 消息分发器
                .addLast(LOGGINF_REGIST)
                // 断线处理
                .addLast(ADAPT_HANDLER);

    }
}
