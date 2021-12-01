package com.litao.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
        // 目标方地址
        private Integer targetID;
        // 发送方地址
        private Integer sourceID;
        //
        private Integer messageID;
        // 无人机状态数据体
        private MessageData messageData;

}
