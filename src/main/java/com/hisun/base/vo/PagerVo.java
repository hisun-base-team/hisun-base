/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.vo;

import java.util.List;
/**
 * @author Rocky {rockwithyou@126.com}
 */
public class PagerVo<T> {

	/**
	 * 总数
	 */
	private int total;
	
	/**
	 * 当前页数
	 */
	private int pageNum;
	
	/**
	 * 数据
	 */
	private List<T> datas;
	
	/**
	 * 分页大小
	 */
	private int pageSize;
	
	/**
	 * 总页数
	 */
	private int pageCount;
	
	public PagerVo(List<T> datas,int total, int pageNum,int pageSize){
		this.datas = datas;
		this.total = total;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageCount = (int)Math.ceil((double)total/pageSize);
	}

	public int getTotal() {
		return total;
	}
	public int getPageNum() {
		return pageNum;
	}
	public List<T> getDatas() {
		return datas;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	
}
