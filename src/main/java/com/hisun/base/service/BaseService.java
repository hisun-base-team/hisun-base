/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.service;

import com.hisun.base.dao.util.OrderBy;
import com.hisun.base.dao.util.CommonConditionQuery;
import com.hisun.base.dao.util.CommonOrderBy;
import com.hisun.base.dao.util.ConditionQuery;

import java.util.List;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public interface BaseService <E extends java.io.Serializable, PK extends java.io.Serializable>{
    
	
	 PK save(E entity);
     void saveOrUpdate(E entity);
     void deleteByPK(PK pk);
     void delete(E entity);
     void deleteList(List<E> entityList);
     void deleteBatch(CommonConditionQuery condition);
     void update(E entity);
     void updateList(List<E> entityList);
     E getPK(PK pk);
     E getByPK(PK pk);
     E loadByPK(PK pk);
     boolean exists(PK pk);
     List<E> list();
     List<E> list(boolean cacheable);
     <T> T count();
     <T> T count(boolean cacheable);

    
     <T> T count(ConditionQuery query);
     <T> T count(ConditionQuery query, boolean cacheable);
     List<E> list(ConditionQuery query, OrderBy orderBy);
     List<E> list(ConditionQuery query, OrderBy orderBy, boolean cacheable);
     List<E> list(ConditionQuery query, OrderBy orderBy,
                        final int pageNum, final int pageSize);
     List<E> list(ConditionQuery query, OrderBy orderBy,
                        final int pageNum, final int pageSize, boolean cacheable);
    
     <T> T count(CommonConditionQuery query);
     <T> T count(CommonConditionQuery query, boolean cacheable);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy, boolean cacheable);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize, boolean cacheable);
    
     <T> List<T> list(String hql, List<Object> paramList, int pageNum, int pageSize);
     int count(String hql, List<?> paramList);
     int addTombstones(CommonConditionQuery query);
     int removeTombstones(CommonConditionQuery query);
    
     void addTombstonePK(PK pk);
     void addTombstone(E entity);
     void removeTombstone(E entity);
     int update(E entity, String[] columnNames);
}
