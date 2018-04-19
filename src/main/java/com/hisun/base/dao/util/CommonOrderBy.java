/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.dao.util;

import com.google.common.collect.Lists;
import com.hisun.util.JacksonUtil;

import java.io.Serializable;
import java.util.List;


/**
 * @author Rocky {rockwithyou@126.com}
 */
public class CommonOrderBy implements Serializable{

	
	private List<CommonOrder> orders = Lists.newLinkedList();
	public CommonOrderBy(){
		orders = Lists.newLinkedList();
	}
	public List<CommonOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<CommonOrder> orders) {
		this.orders = orders;
	}
	public void add(CommonOrder order){
		this.orders.add(order);
	}
	
	@Override
	public String toString(){
		return JacksonUtil.nonDefaultMapper().toJson(this);
	}

}
