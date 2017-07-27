package org.fablat.resource.model.daoimpl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.fablat.resource.model.dao.GenericDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> domainClass;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	private Class<T> getDomainClass() {
		if (domainClass == null) {
			ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
			this.domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
		}
		return domainClass;
	}

	public String getDomainClassName() {
		return getDomainClass().getName();
	}

	@Transactional
	public T findById(ID id) {
		T entity;

		entity = (T) getSession().get(getDomainClass(), id);

		return entity;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<T> findAll() {
		List<T> entities;

		entities = (List<T>) getSession().createQuery("from " + getDomainClassName() + " x").list();

		return entities;
	}

	@Transactional
	public T makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	@Transactional
	public void makeTransient(T entity) {
		getSession().delete(entity);
	}

	@Transactional
	public void flush() {
		getSession().flush();
	}

	@Transactional
	public void clear() {
		getSession().clear();
	}
	/*
	 * En caso de usar Criteria API protected List<T>
	 * findByCriteria(Criterion... criterion) { Criteria crit =
	 * getSession().createCriteria(getPersistentClass());
	 * 
	 * for (Criterion c : criterion) { crit.add(c); }
	 * 
	 * return crit.list(); }
	 */
}
