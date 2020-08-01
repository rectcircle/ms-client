package cn.rectcircle.ms.client;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-08-01
 * @version 0.0.1
 */
public class MsClientNetworkException extends RuntimeException {

    private static final long serialVersionUID = -7918320924395613910L;

    public MsClientNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}