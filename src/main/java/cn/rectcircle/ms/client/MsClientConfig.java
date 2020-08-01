package cn.rectcircle.ms.client;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public interface MsClientConfig<T> {

    /**
     * 获取 Service 坐标
     * @return
     */
    T getServiceCoordinate();

}