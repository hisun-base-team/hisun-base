/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.exception;
/**
 * @author Rocky {rockwithyou@126.com}
 */
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMsg;



	public GenericException() {
		super();
	}
	public GenericException(String errorCode,String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	public GenericException(String message) {
		super(message);
	}
	public GenericException(Throwable cause) {
		super(cause);
	}
	public GenericException(String message, Throwable cause) {
		super(message, cause);
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
