/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.vo;

import com.hisun.base.entity.TombstoneEntity;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public class TombstoneVo extends BaseVo{

	protected int tombstone= TombstoneEntity.TOMBSTONE_FALSE;
	protected String tombstoneStr="正常";
	
	public int getTombstone() {
		return tombstone;
	}
	public void setTombstone(int tombstone) {
		this.tombstone = tombstone;
		if(this.tombstone==TombstoneEntity.TOMBSTONE_FALSE){
			tombstoneStr="正常";
		}else{
			tombstoneStr="已删除";
		}
	}
	public String getTombstoneStr() {
		return tombstoneStr;
	}
	public void setTombstoneStr(String tombstoneStr) {
		this.tombstoneStr = tombstoneStr;
	}
	
}
