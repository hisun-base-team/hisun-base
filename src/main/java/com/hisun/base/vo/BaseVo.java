/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.vo;

import org.joda.time.DateTime;

import java.util.Date;
/**
 * @author Rocky {rockwithyou@126.com}
 */
public class BaseVo {
	
	protected String createUserId;
	protected String createUserName;
	protected Date createDate;
	protected String createDateStr;
	protected String updateUserId;
	protected String updateUserName;
	protected Date updateDate;
	protected String updateDateStr;
	
	public String getCreateUser() {
		return createUserId;
	}
	public void setCreateUser(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		if(this.createDate!=null){
			this.createDateStr = new DateTime(this.createDate).toString("yyyy-MM-dd HH:mm:ss");
		}else{
			this.createDateStr="";
		}
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public String getUpdateUser() {
		return updateUserId;
	}
	public void setUpdateUser(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		if(this.updateDate!=null){
			this.updateDateStr = new DateTime(this.updateDate).toString("yyyy-MM-dd HH:mm:ss");
		}else{
			this.updateDateStr="";
		}
	}
	public String getUpdateDateStr() {
		return updateDateStr;
	}
	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}
	
}
