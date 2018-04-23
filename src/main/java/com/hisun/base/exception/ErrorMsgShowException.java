package com.hisun.base.exception;

public class ErrorMsgShowException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String msg;
	
	public ErrorMsgShowException(String msg){
		super(msg);
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public String toString(){
		return msg;
	}
}
