package com.jackeroo.oauth.common.vo;

import com.jackeroo.oauth.common.enums.ResultStatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	private String msg = "ok";

	/**
	 * 返回代码
	 */
	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */
	private Object data;


	public Result() {

	}
    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(ResultStatusCode resultStatusCode, Object data) {
        this(resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
    }

    public Result(int code, String msg) {
        this(code, msg, null);
    }

    public Result(ResultStatusCode resultStatusCode) {
        this(resultStatusCode, null);
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    public static Result ok() {
		return new Result(ResultStatusCode.OK);
	}

	public static Result ok(Object obj) {
        return new Result(ResultStatusCode.OK, obj);
	}

	public static Result error(ResultStatusCode resultStatusCode){
	    return new Result(resultStatusCode);
    }

    public static Result error(ResultStatusCode resultStatusCode, Object obj){
        return new Result(resultStatusCode, obj);
    }

    public static Result error(String msg){
	    return new Result(5000, msg, null);
    }
}
