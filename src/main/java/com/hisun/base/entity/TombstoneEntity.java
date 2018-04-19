/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Rocky {rockwithyou@126.com}
 */
@MappedSuperclass
public abstract class TombstoneEntity extends BaseEntity{
	
	public static final int TOMBSTONE_FALSE=0;
	public static final int TOMBSTONE_TRUE=1;
	protected int tombstone=TOMBSTONE_FALSE;

	@Column(name="tombstone",nullable=false)
	public int getTombstone() {
		return tombstone;
	}
	public void setTombstone(int tombstone) {
		this.tombstone = tombstone;
	}
	
	
}
