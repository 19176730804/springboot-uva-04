package com.litao.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    // 无人机前进速度
    private float positonX;
    // 无人机上升速度
    private float positonY;
    // 无人机Z轴速度
    private float positonZ;
    //
    private float rotationX;
    //
    private float rotationY;
    //
    private float rotationZ;
    // 无人机经度
    private double droneLocationLat;
    // 无人机纬度
    private double droneLocationLng;
    // 无人机高度
    private double droneLocationArt;
    // 时间撮
    private Integer timeStamp;
    // 电量
    private Integer value;

}
