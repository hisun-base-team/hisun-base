/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */
package com.hisun.base.dao;

import com.hisun.base.dao.util.OrderBy;
import com.hisun.base.dao.util.CommonConditionQuery;
import com.hisun.base.dao.util.CommonOrderBy;
import com.hisun.base.dao.util.ConditionQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Map;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public interface BaseDao<E extends java.io.Serializable, PK extends java.io.Serializable> {

     Session getSession();
     PK save(E entity);
     void saveOrUpdate(E entity);
     void deleteByPK(PK pk);
     void delete(E entity);
     void deleteBatch(CommonConditionQuery condition);
     void update(E entity);
     void evict(E entity);
     E getPK(PK pk);
     E getByPK(PK pk);
     E loadByPK(PK pk);
     boolean exists(PK pk);
    
     void evict();
     List<E> list();
     List<E> list(boolean cacheable);
     <T> T count();
     <T> T count(boolean cacheable);
     void flush();
     void clear();
    
     int executeBulk(final String hql, CommonConditionQuery query);
     int executeNativeBulk(final String nativeSQL, CommonConditionQuery query);
     int executeNativeBulk(final String nativeSQL, List<Object> paramList);
    
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
     <T> T count(String hql, CommonConditionQuery query);
     <T> T countByHql(String hql, CommonConditionQuery query);
     int countBySql(String sql, Map<String, Object> paramMap);
     <T> T count(String hql, CommonConditionQuery query, boolean cacheable);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy, boolean cacheable);
     List<E> list(String hql, CommonConditionQuery query, CommonOrderBy orderBy);
     List<E> list(String hql, CommonConditionQuery query, CommonOrderBy orderBy, boolean cacheable);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize);
     List<E> list(CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize, boolean cacheable);
     List<E> list(String hql, CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize);
     List<E> list(String hql, CommonConditionQuery query, CommonOrderBy orderBy,
                        final int pageNum, final int pageSize, boolean cacheable);
    
     <T> List<T> find(String hql, CommonConditionQuery query, CommonOrderBy orderBy);
     <T> List<T> find(String hql, CommonConditionQuery query, CommonOrderBy orderBy,
                            final int pageNum, final int pageSize);
    
     <T> T nativeCount(String sql, CommonConditionQuery query);
     Long nativeCount(String sql, Map<String, Object> arg);
     <T> List<T> nativeList(String sql, CommonConditionQuery query, CommonOrderBy orderBy);
     <T> List<T> nativeList(String sql, CommonConditionQuery query, CommonOrderBy orderBy,
                                  final int pageNum, final int pageSize);
     <T> List<T> nativeList(String sql, Map<String, Object> arg, final int pageNum, final int pageSize);
    
     <T> List<T> nativeList(Class<T> c, String sql, Map<String, Object> arg, final int pageNum, final int pageSize);
    
     <T> List<T> nativeList(Class<T> c, String sql, CommonConditionQuery condition, CommonOrderBy orderBy,
                                  final int pageNum, final int pageSize);
     <T> List<T> nativeList(Class<T> c, String sql, Map<String, Object> paramMap);
     int addTombstones(CommonConditionQuery query);
     int removeTombstones(CommonConditionQuery query);
     <T> List<T> list(String hql, List<Object> paramList, int pageNum, int pageSize);
     <T> List<T> list(String hql, Map<String, Object> paramMap, int pageNum, int pageSize);
     <T> List<T> list(String hql, List<Object> paramList);
     <T> List<T> list(List<Criterion> criterionList, List<Order> orderList, int pageNum, int pageSize);
     int count(String hql, List<Object> paramList);
     int count(String hql, Map<String, Object> paramMap);
     int count(List<Criterion> criterionList);
     int update(E entity, String... columnNames);
     int update(String sql, Map<String, Object> paramMap);
     List<Map> list(String hql, Map<String, Object> paramMap);
     List<Map> nativeList(String sql, Map<String, Object> paramMap);


}
