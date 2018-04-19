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
public abstract  class AbstractRoleResourcePrivilege {

    protected String sqlFilterExpress;
    protected String selectedExpress;

//    public abstract AbstractRole getAbstractRole();
//    public abstract  AbstractResource getAbstractResource();
//    public abstract AbstractPrivilege getAbstractPrivilege();

    @Column(name="sql_filter_express",nullable=false)
    public String getSqlFilterExpress() {
        return sqlFilterExpress;
    }
    public void setSqlFilterExpress(String sqlFilterExpress) {
        this.sqlFilterExpress = sqlFilterExpress;
    }
    @Column(name="selected_express",nullable=false)
    public String getSelectedExpress() {
        return selectedExpress;
    }
    public void setSelectedExpress(String selectedExpress) {
        this.selectedExpress = selectedExpress;
    }
}