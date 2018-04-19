/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.dao.util;

import com.hisun.util.JacksonUtil;

import java.io.Serializable;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public class CommonOrder implements Serializable{

	public static final String ASC="asc";
	public static final String DESC="desc";
	
	private String logic;
	private String orderColumn;
	
	private CommonOrder(){
		
	}
	
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getLogic() {
		return logic;
	}
	private void setLogic(String logic) {
		this.logic = logic;
	}
	private static CommonOrder build(String logic ,String orderColumn){
		CommonOrder order = new CommonOrder();
		order.setOrderColumn(orderColumn);
		order.setLogic(logic);
		return order;
	}
	public static CommonOrder asc(String orderColumn){
		return build(ASC,orderColumn);
	}
	public static CommonOrder desc(String orderColumn){
		return build(DESC,orderColumn);
	}
	@Override
	public String toString(){
		return JacksonUtil.nonDefaultMapper().toJson(this);
	}

}
