package cn.rectcircle.ms.client;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-31
 * @version 0.0.1
 */
public class MsClientException extends RuntimeException {

    private static final long serialVersionUID = -6126196054468220610L;

    public MsClientException(String message) {
        super(message);
    }

    
    public MsClientException(String message, Throwable cause) {
        super(message, cause);
    }



}
