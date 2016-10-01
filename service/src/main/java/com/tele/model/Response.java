package com.tele.model;

import lombok.Data;

/**
 * @author zhangleimin
 * @package com.tele.model
 * @date 16-9-29
 */
@Data
public class Response<T> {

    private boolean success;
    private String errMsg;
    private T data;
}
