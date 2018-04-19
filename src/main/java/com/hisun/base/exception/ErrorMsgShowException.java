package com.hisun.base.exception;
/**
 * 
 *<p>类名称：</p>
 *<p>类描述: </p>
 *<p>公司：湖南海数互联信息技术有限公司</p>
 *@创建人：lyk
 *@创建时间：2015年4月25日
 *@创建人联系方式：
 *@version
 */
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
