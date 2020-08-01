package cn.rectcircle.ms.discovery;

import java.util.List;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @param <SC> SC 服务坐标
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
@FunctionalInterface
public interface ServiceDiscovery<SC> {

    /**
     * 执行一次服务发现，获取一个服务列表
     * @param serviceCoordinate 服务坐标
     * @return 服务发现列表
     */
    List<ServiceNode> discovery(SC serviceCoordinate);

}