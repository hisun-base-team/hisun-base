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
public class CommonRestrictions implements Serializable{
	
	public static final String AND="and";
	public static final String OR="or";
	
	
    private String logic = null;
	private String condition = null;
    private String name = null;
    private Object value = null;
    
    private CommonRestrictions(){}

	public String getLogic() {
		return logic;
	}
	private void setLogic(String logic) {
		this.logic = logic;
	}

	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
    
    public  void  add (String condition,String name ,Object value){
    	this.condition =   condition;
    	this.name = name;
    	this.value = value;
    }
    
    private static CommonRestrictions build(String logic,String condition,String name ,Object value){
    	CommonRestrictions commonRestrictions = new CommonRestrictions();
    	commonRestrictions.setLogic(logic);
    	commonRestrictions.setCondition(condition);
    	commonRestrictions.setName(name);
    	commonRestrictions.setValue(value);
    	return commonRestrictions;
    }
    
    public static CommonRestrictions  and (String condition,String name ,Object value){
    	return build(AND, condition, name, value);
    }
    
    
    
    public static CommonRestrictions  or (String condition,String name ,Object value){
    	return build(OR, condition, name, value);
    }
    
    @Override
	public String toString(){
		return JacksonUtil.nonDefaultMapper().toJson(this);
	}

}
