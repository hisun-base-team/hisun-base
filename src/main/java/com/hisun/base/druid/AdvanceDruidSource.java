/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.druid;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public class AdvanceDruidSource extends DruidDataSource{

	@Override
	public void setUsername(String username) {
		try {
			if (StringUtils.length(username) == 88) {
				username = ConfigTools.decrypt(username);
			}
		} catch (Exception e) {
			throw new RuntimeException("解析数据库用户名异常!",e);
		}
		super.setUsername(username);
	}
}
