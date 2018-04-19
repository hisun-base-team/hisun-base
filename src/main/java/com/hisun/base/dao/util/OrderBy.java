/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */
package com.hisun.base.dao.util;

import com.google.common.collect.Lists;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import java.util.List;
/**
 * @author Rocky {rockwithyou@126.com}
 */
public class OrderBy {
	
	private List<Order> orders = Lists.newLinkedList();
	public OrderBy(){}
	public void add(Order order){
		this.orders.add(order);
	}
    public void build(Criteria criteria) {
        for(Order order : orders) {
            criteria.addOrder(order);
        }
    }

}
