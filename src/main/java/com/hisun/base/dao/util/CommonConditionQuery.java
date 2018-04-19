
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
public class CommonConditionQuery implements Serializable {

    private List<CommonRestrictions> restrictions = Lists.newLinkedList();
    private boolean isOnlySetQueryParams = false;


    public CommonConditionQuery() {
        restrictions = Lists.newLinkedList();
    }

    public CommonConditionQuery(boolean isOnlySetQueryParams) {
        this.isOnlySetQueryParams = isOnlySetQueryParams;
        restrictions = Lists.newLinkedList();
    }

    public List<CommonRestrictions> getRestrictions() {
        return restrictions;
    }


    public void setRestrictions(List<CommonRestrictions> restrictions) {
        this.restrictions = restrictions;
    }


    public void add(CommonRestrictions restrictions) {
        this.restrictions.add(restrictions);
    }

    @Override
    public String toString() {
        return JacksonUtil.nonDefaultMapper().toJson(this);
    }

    public boolean isOnlySetQueryParams() {
        return isOnlySetQueryParams;
    }

    public void setOnlySetQueryParams(boolean isOnlySetQueryParams) {
        this.isOnlySetQueryParams = isOnlySetQueryParams;
    }
}
