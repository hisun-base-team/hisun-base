/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.service.impl;


import com.hisun.base.dao.BaseDao;
import com.hisun.base.dao.impl.BaseDaoImpl;
import com.hisun.base.dao.util.CommonConditionQuery;
import com.hisun.base.dao.util.CommonOrderBy;
import com.hisun.base.dao.util.ConditionQuery;
import com.hisun.base.dao.util.OrderBy;
import com.hisun.base.entity.TombstoneEntity;
import com.hisun.base.service.BaseService;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import java.util.List;


/**
 * @author Rocky {rockwithyou@126.com}
 */
public abstract class BaseServiceImpl<E extends java.io.Serializable, PK extends java.io.Serializable>
implements BaseService<E, PK> {
	
	
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);

	protected BaseDao<E, PK> baseDao;
	
	public abstract void setBaseDao(BaseDao<E, PK> baseDao);
	
	public PK save(E entity){
		return this.baseDao.save(entity);
	}
	
    public void saveOrUpdate(E entity){
    	this.baseDao.saveOrUpdate(entity);
    }
    
    public void deleteByPK(PK pk){
    	this.baseDao.deleteByPK(pk);
    }
    
    public void delete(E entity){
    	this.baseDao.delete(entity);
    }
    public void deleteBatch(CommonConditionQuery condition){
    	this.baseDao.deleteBatch(condition);
    }
    public void update(E entity){
    	this.baseDao.update(entity);
    }
    
    public E getByPK(PK pk){
    	return this.baseDao.getByPK(pk);
    }
    
	public E getPK(PK pk) {
		return this.baseDao.getPK(pk);
	}

	public E loadByPK(PK pk){
    	return this.baseDao.loadByPK(pk);
    }
    
    public boolean exists(PK pk){
    	return this.baseDao.exists(pk);
    }
    
    public List<E> list(){
    	return this.baseDao.list();
    }
    
    public List<E> list(boolean cacheable){
    	return this.baseDao.list(cacheable);
    }
    
    public <T> T count(){
    	return this.baseDao.count();
    }
    
    public <T> T count(boolean cacheable){
    	return this.baseDao.count(cacheable);
    }
    
    public <T> T count (ConditionQuery query){
    	return this.baseDao.count(query);
    }
    
    public <T> T count (ConditionQuery query,boolean cacheable){
    	return this.baseDao.count(query,cacheable);
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy){
    	return this.baseDao.list(query, orderBy);
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,boolean cacheable){
    	return this.baseDao.list(query, orderBy,cacheable);
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,final int pageNum, final int pageSize){
    	return this.baseDao.list(query, orderBy, pageNum, pageSize);
    }
    
    public List<E> list(ConditionQuery query, OrderBy orderBy,
    		final int pageNum, final int pageSize,boolean cacheable){
    	return this.baseDao.list(query, orderBy, pageNum, pageSize,cacheable);
    }
    
    public <T> T count(CommonConditionQuery query){
    	return this.baseDao.count(query);
    }
    
    public <T> T count(CommonConditionQuery query,boolean cacheable){
    	return this.baseDao.count(query);
    }

    public List<E> list(CommonConditionQuery query,CommonOrderBy orderBy){
    	return this.baseDao.list(query, orderBy);
    }
    
    public List<E> list(CommonConditionQuery query,CommonOrderBy orderBy,boolean cacheable){
    	return this.baseDao.list(query, orderBy,cacheable);
    }
    
    public List<E> list(CommonConditionQuery query,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize){
    	return this.baseDao.list(query, orderBy, pageNum, pageSize);
    }
    
    public List<E> list(CommonConditionQuery query,CommonOrderBy orderBy,
    		final int pageNum, final int pageSize,boolean cacheable){
    	return this.baseDao.list(query, orderBy, pageNum, pageSize,cacheable);
    }
    
    
	public void addTombstonePK(PK pk) {
		addTombstone(getPK(pk));
	}

	public void addTombstone(E entity){
    	if(entity instanceof TombstoneEntity){
    		((TombstoneEntity) entity).setTombstone(TombstoneEntity.TOMBSTONE_TRUE);
    	}else{
    		logger.warn("Entity is not TombstoneEntity");
    	}
    }
    public void removeTombstone(E entity){
    	if(entity instanceof TombstoneEntity){
    		((TombstoneEntity) entity).setTombstone(TombstoneEntity.TOMBSTONE_FALSE);
    	}else{
    		logger.warn("Entity is not TombstoneEntity");
    	}
    }
    public int addTombstones(CommonConditionQuery query){
    	return this.baseDao.addTombstones(query);
    }
    public int removeTombstones(CommonConditionQuery query){
    	return this.baseDao.removeTombstones(query);
    }

	@Override
	public int update(E entity, String[] columnNames) {
		return this.baseDao.update(entity, columnNames);
	}

	public <T> List<T> list(String hql, List<Object> paramList, int pageNum,
			int pageSize) {
		return this.baseDao.list(hql, paramList, pageNum, pageSize);
	}

	public <T> List<T> list(String hql,List<Object> paramList){
		return this.baseDao.list(hql,paramList);
	}

	public int count(String hql, List<?> paramList) {
		Query query = this.baseDao.getSession().createQuery(hql);
		for(int i=0;i<paramList.size();i++){
			query.setParameter(i, paramList.get(i));
		}
		return ((Number)query.uniqueResult()).intValue();
	}

	@Override
	public void deleteList(List<E> entityList) {
		if(entityList!=null){
			for(E e:entityList){
				this.delete(e);
			}
		}
	}

	@Override
	public void updateList(List<E> entityList) {
		if(entityList!=null){
			for(E e:entityList){
				this.update(e);
			}
		}
	}
	
}
