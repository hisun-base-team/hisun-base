/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.dao.impl;

import com.hisun.base.dao.BaseDao;
import com.hisun.base.dao.util.*;
import com.hisun.base.entity.TombstoneEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public abstract class BaseDaoImpl<E extends java.io.Serializable, PK extends java.io.Serializable>
implements BaseDao<E, PK> {

	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);
	private static final String SPACE=" ";

    @Resource
    private SessionFactory sessionFactory;
    protected Class<E> entityClass = null;
	//private String pkName = null;
    private String HQL_ALL = null;
    private String HQL_COUNT_ALL=null;
    private String HQL_UPDATE_TOMBSTONE=null;


    public BaseDaoImpl() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        //Field[] fields = this.entityClass.getDeclaredFields();
        //for(Field f : fields) {
        //    if(f.isAnnotationPresent(Id.class)) {
        //        this.pkName = f.getName();
        //    }
        //}
        HQL_ALL = " select "+entityClass.getSimpleName()+" from "+entityClass.getSimpleName()+" "+entityClass.getSimpleName();
        HQL_COUNT_ALL = " select count(*) from  "+entityClass.getSimpleName()+" "+entityClass.getSimpleName();
        HQL_UPDATE_TOMBSTONE =" update "+entityClass.getSimpleName()+" "+entityClass.getSimpleName()+" set "+
        		entityClass.getSimpleName()+".tombstone=:tombstone";
    }
    

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public PK save(E entity) {
        return (PK) getSession().save(entity);
    }
    public void saveOrUpdate(E entity){
    	getSession().saveOrUpdate(entity);
    }
    public void deleteByPK(PK pk){
    	getSession().delete(this.getByPK(pk));
    }
    public void delete(E entity){
    	getSession().delete(entity);
    }
    public void deleteBatch(CommonConditionQuery condition){
    	String hql = "delete "+entityClass.getSimpleName()+" "+entityClass.getSimpleName();
    	this.executeBulk(hql, condition);
    }
    public void update(E entity){
    	getSession().update(entity);
    }
    
    public E getByPK(PK pk){
    	return (E)getSession().get(entityClass, pk);
    }
	public E getPK(PK pk) {
		ClassMetadata cm = getSession().getSessionFactory().getClassMetadata(entityClass);
		String primaryKeyName = cm.getIdentifierPropertyName();
		StringBuilder hql=new StringBuilder();
		hql.append("from ").append(cm.getEntityName()).append(" where ").append(primaryKeyName).append(" =:").append(primaryKeyName);
		Query query = getSession().createQuery(hql.toString());
		query.setParameter(primaryKeyName, pk);
		return (E) query.uniqueResult();
	}


	public E loadByPK(PK pk){
    	return (E)getSession().load(entityClass, pk);
    }
    public void evict(E entity){
    	getSession().evict(entity);
    }
    public void evict(){
    	this.sessionFactory.evict(entityClass);
    }
    public boolean exists(PK pk){
    	return (this.getByPK(pk)!=null);
    }

    private void setParameters(Query query,CommonConditionQuery condition) {
    	List<CommonRestrictions> restrictions = condition.getRestrictions();
        for (CommonRestrictions restriction :restrictions) {

        	if (restriction.getName() == null) {
        		break;
        	}
            if(restriction.getValue() instanceof Date) {
                query.setTimestamp(restriction.getName(), (Date)restriction.getValue());
            } else if(restriction.getValue()  instanceof Collection) {
                query.setParameterList(restriction.getName(), (Collection)restriction.getValue());
            } else{
            	query.setParameter(restriction.getName(), restriction.getValue());
            }
        }
    }
    
    private void appendcondition(StringBuilder sqlsb,CommonConditionQuery condition) {
    	if(condition.isOnlySetQueryParams()){
    		if(logger.isDebugEnabled()){
    			logger.info("condition sql = "+sqlsb);
    		}
    		return;
    	}
    	List<CommonRestrictions> restrictions = condition.getRestrictions();
    	int i = 0;
    	for (CommonRestrictions restriction :restrictions) {
			if(restriction.getCondition()==null 
					|| "".equals(restriction.getCondition())){
				continue;
			}
			if(i == 0){
				Pattern pattern = Pattern.compile("\\s+where\\s+");
				Matcher matcher = pattern.matcher(sqlsb.toString().toLowerCase());
				if(!matcher.find()){
					sqlsb.append(SPACE).append("where").append(SPACE);
				}
				sqlsb.append(restriction.getCondition());
			}else{
				//sqlsb.append(" and ");//不默认增加运算关系，由condition指明
				sqlsb.append(SPACE).append(restriction.getLogic()).append(SPACE);
				sqlsb.append(SPACE).append(restriction.getCondition()).append(SPACE);
			}
			i++;
		}
		if(logger.isDebugEnabled()){
			logger.info("condition sql = "+sqlsb);
		}
    }
    
    private void appendOrderBys(StringBuilder sqlsb,CommonOrderBy orderBy) {
		List<CommonOrder> orders = orderBy.getOrders();
    	int i = 0;
		for(CommonOrder order : orders){
			if(i == 0){
				sqlsb.append(SPACE).append("order by");
				sqlsb.append(SPACE).append(order.getOrderColumn()).append(SPACE);
				sqlsb.append(order.getLogic());
			}else{
				sqlsb.append(",");
				sqlsb.append(order.getOrderColumn());
				sqlsb.append(SPACE).append(order.getLogic()).append(SPACE);
			}
			i++;
		}
		if(logger.isDebugEnabled()){
			logger.info("order by  sql = "+sqlsb);
		}
    }
    
    public int executeBulk(final String hql,CommonConditionQuery condition) {
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null){
        	this.setParameters(query, condition);
        }
        Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }
    
    public int executeNativeBulk(final String nativeSQL, CommonConditionQuery condition) {
    	StringBuilder nativeSQLBuffer = new StringBuilder(nativeSQL);
    	if(condition!=null){
    		this.appendcondition(nativeSQLBuffer, condition);
    	}
        Query query = getSession().createSQLQuery(nativeSQLBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }
 
    
    @Override
	public int executeNativeBulk(String nativeSQL, List<Object> paramList) {
    	StringBuilder nativeSQLBuffer = new StringBuilder(nativeSQL);
        Query query = getSession().createSQLQuery(nativeSQLBuffer.toString());
        for(int i=0;i<paramList.size();i++){
        	query.setParameter(i, paramList.get(i));
        }
        Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
	}


	public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }
    
    
    public List<E> list(){
    	return this.list(false);
    }
    
    public List<E> list(boolean cacheable){
    	Criteria criteria = getSession().createCriteria(this.entityClass);
    	return criteria.setCacheable(cacheable).list();
    }
    
    public <T> T count(){
      	 return count(false);
    }
    
    public <T> T count(boolean cacheable){
     	 Criteria criteria = getSession().createCriteria(this.entityClass);
        criteria.setProjection(Projections.rowCount());
       return (T) criteria.setCacheable(cacheable).uniqueResult();
   }
    
    public <T> T count (ConditionQuery query){
   	 return this.count(query, false);
   }
    
    public <T> T count (ConditionQuery query,boolean cacheable){
      	 Criteria criteria = getSession().createCriteria(this.entityClass);
           query.build(criteria);
           criteria.setProjection(Projections.rowCount());
          return (T) criteria.setCacheable(cacheable).uniqueResult();
      }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy) {
       return this.list(query, orderBy,false);
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,boolean cacheable) {
        Criteria criteria = getSession().createCriteria(this.entityClass);
        query.build(criteria);
        orderBy.build(criteria);
        return criteria.setCacheable(cacheable).list();
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,final int pageNum, final int pageSize) {
    	return this.list(query, orderBy, pageNum, pageSize, false);
    }
    
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,
    		final int pageNum, final int pageSize,boolean cacheable) {
    	Criteria criteria = getSession().createCriteria(this.entityClass);
        query.build(criteria);
        orderBy.build(criteria);
        return criteria.setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).setCacheable(cacheable).list();
    }
    
    public <T> List<T> list(Criteria criteria) {
        return criteria.setCacheable(true).list();
    }

    public <T> T unique(Criteria criteria) {
        return (T) criteria.setCacheable(true).uniqueResult();
    }

    public <T> List<T> list(DetachedCriteria criteria) {
        return list(criteria.getExecutableCriteria(getSession()));
    }

    public <T> T unique(DetachedCriteria criteria) {
        return (T) unique(criteria.getExecutableCriteria(getSession()));
    }
    
    public <T> T count(CommonConditionQuery condition){
    	return this.count(this.HQL_COUNT_ALL, condition);
    }
    
    public <T> T count(CommonConditionQuery condition,boolean cacheable){
    	return this.count(this.HQL_COUNT_ALL, condition,cacheable);
    }
    
    public <T> T count(String hql,CommonConditionQuery condition){
    	return this.count(this.HQL_COUNT_ALL, condition,false);
    }
    
    public <T> T countByHql(String hql,CommonConditionQuery query){
    	return this.count(hql,query,false);
    }
    
    public <T> T count(String hql,CommonConditionQuery condition,boolean cacheable){
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null ){
    		this.appendcondition(hqlBuffer, condition);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
       return (T) query.setCacheable(cacheable).uniqueResult();
    }
    
    
    public List<E> list(CommonConditionQuery condition,CommonOrderBy orderBy){
    	return this.list(this.HQL_ALL, condition, orderBy);
    }
    
    public List<E> list(CommonConditionQuery condition,CommonOrderBy orderBy,boolean cacheable){
    	return this.list(this.HQL_ALL, condition, orderBy,cacheable);
    }
    
    public List<E> list(String hql,CommonConditionQuery condition,CommonOrderBy orderBy){
    	return this.list(hql, condition, orderBy, false);
    }
    public List<E> list(String hql,CommonConditionQuery condition,CommonOrderBy orderBy,boolean cacheable){
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
    	if(orderBy!=null){
        	this.appendOrderBys(hqlBuffer, orderBy);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
       return query.setCacheable(cacheable).list();
    }
    
    public List<E> list(CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize){
    	return this.list(this.HQL_ALL, condition, orderBy, pageNum, pageSize);
    }
    
  /*  @Override
	public <T> List<T> list(Class cls, String hql,
			CommonConditionQuery condition, CommonOrderBy orderBy, int pageNum,
			int pageSize) {
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(hqlBuffer, orderBy);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        return query.setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
	}*/


	public List<E> list(CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize,boolean cacheable){
    	return this.list(this.HQL_ALL, condition, orderBy, pageNum, pageSize,cacheable);
    }
    
    public List<E> list(String hql,CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize){
    	return this.list(hql, condition, orderBy, pageNum, pageSize, false);
    }
    
    public List<E> list(String hql,CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize,boolean cacheable){
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(hqlBuffer, orderBy);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        if(pageNum<1 || pageSize<1){
        	return query.setCacheable(cacheable).list();
        }else{
        	return query.setCacheable(cacheable).setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
        }
    }
    
    public <T> List<T> find(String hql,CommonConditionQuery condition,CommonOrderBy orderBy){
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(hqlBuffer, orderBy);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
       return query.setCacheable(false).list();
    }
    
    public <T> List<T> find(String hql,CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize){
    	StringBuilder hqlBuffer = new StringBuilder(hql);
    	if(condition!=null){
    		this.appendcondition(hqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(hqlBuffer, orderBy);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        if(pageNum<1 || pageSize<1){
        	return query.setCacheable(false).list();
        }else{
        	return query.setCacheable(false).setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
        }
    }
    
    public Long nativeCount(String sql,Map<String, Object> arg){
    	StringBuilder sqlBuffer = new StringBuilder();
    	sqlBuffer.append("select count(1) from (").append(sql).append(") temp");
    	Query query = getSession().createSQLQuery(sqlBuffer.toString());
    	query.setProperties(arg);
    	Object count=query.setCacheable(false).uniqueResult();
    	if(count instanceof BigInteger){
    		return ((BigInteger)count).longValue();
    	}else{
    		return (Long)count;
    	}
    		
    }
    
	public <T> List<T> nativeList(String sql,Map<String, Object> arg,final int pageNum, final int pageSize){
    	StringBuilder sqlBuffer = new StringBuilder(sql);
        Query query = getSession().createSQLQuery(sqlBuffer.toString());
        query.setProperties(arg);
        if(pageNum<1 || pageSize<1){
        	return query.setCacheable(false).list();
        }else{
        	return query.setCacheable(false).setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
        }
    }
    
    @Override
	public <T> List<T> nativeList(Class<T> c, String sql, Map<String, Object> paramMap) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(c);
		query.setProperties(paramMap);
		return query.list();
	}
    
    @Override
	public <T> List<T> nativeList(Class<T> c, String sql, Map<String, Object> paramMap, int pageNum, int pageSize) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(c);
		query.setProperties(paramMap);
		query.setFirstResult(pageSize * (pageNum - 1));
		query.setMaxResults(pageSize);
		return query.list();
	}


	public <T> T nativeCount(String sql,CommonConditionQuery condition){
    	StringBuilder sqlBuffer = new StringBuilder(sql);
    	if(condition!=null ){
    		this.appendcondition(sqlBuffer, condition);
    	}
        Query query = getSession().createSQLQuery(sqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
       return (T) query.setCacheable(false).uniqueResult();
    }
   
    public <T> List<T> nativeList(String sql,CommonConditionQuery condition,CommonOrderBy orderBy){
    	StringBuilder sqlBuffer = new StringBuilder(sql);
    	if(condition!=null ){
    		this.appendcondition(sqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(sqlBuffer, orderBy);
    	}
        Query query = getSession().createSQLQuery(sqlBuffer.toString());
        if(condition!=null){
        	this.setParameters(query, condition);
        }
       return query.setCacheable(false).list();
    }

    @Override
	public <T> List<T> nativeList(Class<T> c, String sql,
			CommonConditionQuery condition, CommonOrderBy orderBy, int pageNum,
			int pageSize) {
    	StringBuilder sqlBuffer = new StringBuilder(sql);
    	if(condition!=null){
    		this.appendcondition(sqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(sqlBuffer, orderBy);
    	}
        SQLQuery query = getSession().createSQLQuery(sqlBuffer.toString());
        if(condition!=null){
        	this.setParameters(query, condition);
        }
        query.addEntity(c);
        if(pageNum<1 || pageSize<1){
        	return query.setCacheable(false).list();
        }else{
        	return query.setCacheable(false).setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
        }
	}


	public <T> List<T> nativeList(String sql,CommonConditionQuery condition,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize){
    	StringBuilder sqlBuffer = new StringBuilder(sql);
    	if(condition!=null){
    		this.appendcondition(sqlBuffer, condition);
    	}
    	if(orderBy!=null ){
        	this.appendOrderBys(sqlBuffer, orderBy);
    	}
        Query query = getSession().createSQLQuery(sqlBuffer.toString());
        if(condition!=null){
        	this.setParameters(query, condition);
        }
        if(pageNum<1 || pageSize<1){
        	return query.setCacheable(false).list();
        }else{
        	return query.setCacheable(false).setFirstResult(pageSize * (pageNum - 1)).setMaxResults(pageSize).list();
        }
    }
    
    
    public int addTombstones(CommonConditionQuery condition){
    	StringBuilder hqlBuffer = new StringBuilder(HQL_UPDATE_TOMBSTONE);
    	if(condition!=null ){
    		this.appendcondition(hqlBuffer, condition);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        Object result = query.setParameter("tombstone", TombstoneEntity.TOMBSTONE_TRUE).executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }
    public int removeTombstones(CommonConditionQuery condition){
    	StringBuilder hqlBuffer = new StringBuilder(HQL_UPDATE_TOMBSTONE);
    	if(condition!=null ){
    		this.appendcondition(hqlBuffer, condition);
    	}
        Query query = getSession().createQuery(hqlBuffer.toString());
        if(condition!=null ){
        	this.setParameters(query, condition);
        }
        Object result = query.setParameter("tombstone", TombstoneEntity.TOMBSTONE_FALSE).executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }


	@Override
	public <T> List<T> list(String hql, List<Object> paramList, int pageNum, int pageSize) {
		Query query = getSession().createQuery(hql);
		for(int i=0;i<paramList.size();i++){
			query.setParameter(i, paramList.get(i));
		}
		query.setFirstResult(pageSize*(pageNum-1));
		query.setMaxResults(pageSize);
		return query.list();
	}
	
	@Override
	public <T> List<T> list(String hql, Map<String,Object> paramMap, int pageNum, int pageSize) {
		Query query = getSession().createQuery(hql);
		query.setProperties(paramMap);
		query.setFirstResult(pageSize*(pageNum-1));
		query.setMaxResults(pageSize);
		return query.list();
	}
	
	@Override
	public <T> List<T> list(String hql, List<Object> paramList) {
		Query query = getSession().createQuery(hql);
		for(int i=0;i<paramList.size();i++){
			query.setParameter(i, paramList.get(i));
		}
		return query.list();
	}
    
	public int update(E entity, String ...columnNames){
		ClassMetadata cm = getSession().getSessionFactory().getClassMetadata(entityClass);
		String primaryKeyName = cm.getIdentifierPropertyName();
		String[] setStr = new String[columnNames.length];
		StringBuilder hql = new StringBuilder("update ");
		hql.append(cm.getEntityName()).append(" set ");
		for(int i=0;i<columnNames.length;i++){
			if(primaryKeyName.equals(columnNames[i])){
				continue;
			}
			setStr[i] = columnNames[i] + " = :" +columnNames[i];
		}
		hql.append(StringUtils.join(setStr, " , "))
			.append(" where ").append(primaryKeyName).append(" = :").append(primaryKeyName);
		Query query = getSession().createQuery(hql.toString());
		for(String columnName : columnNames){
			query.setParameter(columnName, cm.getPropertyValue(entity, columnName));
		}
		query.setParameter(primaryKeyName, cm.getIdentifier(entity, (SessionImplementor) getSession()));
		int result = query.executeUpdate();
		return result;
	}


	@Override
	public int update(String sql, Map<String, Object> paramMap) {
		SQLQuery query = getSession().createSQLQuery(sql);
		Iterator<Entry<String,Object>> it = paramMap.entrySet().iterator();
		Entry<String,Object> entry = null;
		while(it.hasNext()){
			entry = it.next();
			query.setParameter(entry.getKey(), entry.getValue());
		}
		int result = query.executeUpdate();
		return result;
	}
	
	@Override
	public <T> List<T> list(List<Criterion> criterionList, List<Order> orderList,
			int pageNum, int pageSize) {
		Criteria criteria=getSession().createCriteria(entityClass); 
		if(criterionList!=null){
			for(Criterion rest : criterionList){
				criteria.add(rest);
			}
		}
		if(orderList!=null){
			for(Order order : orderList){
				criteria.addOrder(order);
			}
		}
		criteria.setFirstResult(pageSize*(pageNum-1));
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}


	@Override
	public int count(List<Criterion> criterionList) {
		Criteria criteria=getSession().createCriteria(entityClass); 
		if(criterionList!=null){
			for(Criterion rest : criterionList){
				criteria.add(rest);
			}
		}
		criteria.setProjection(Projections.rowCount());
		Number total = (Number) criteria.uniqueResult();
		return total.intValue();
	}

	@Override
	public List<Map> list(String hql, Map<String, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		query.setProperties(paramMap);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}

	@Override
	public List<Map> nativeList(String sql, Map<String, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(paramMap);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.list();
		return list;
	}


	@Override
	public int count(String hql, List<Object> paramList) {
		Query query = getSession().createQuery(hql);
		for(int i=0;i<paramList.size();i++){
			query.setParameter(i, paramList.get(i));
		}
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	@Override
	public int count(String hql, Map<String,Object> paramMap) {
		Query query = getSession().createQuery(hql);
		query.setProperties(paramMap);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	@Override
	public int countBySql(String sql, Map<String, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(paramMap);
		Number number = (Number) query.uniqueResult();
		return number==null?0:number.intValue();
	}

}
