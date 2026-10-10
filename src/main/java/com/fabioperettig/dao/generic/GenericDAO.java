package com.fabioperettig.dao.generic;

import com.fabioperettig.domain.Persistence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public abstract class GenericDAO<T extends Persistence> implements IGenericDAO<T> {

    protected final Class<T> entityClass;

    @PersistenceContext(unitName = "ProjMONOAchievements")
    protected EntityManager entityManager;

    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }


    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T read(Long id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
            return;
        }

        T managedEntity = entityManager.find(entityClass, entity.getId());

        if (managedEntity != null) {
            entityManager.remove(managedEntity);
        }
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT entity FROM "
                + entityClass.getSimpleName()
                + " entity";

        return entityManager
                .createQuery(jpql, entityClass)
                .getResultList();
    }
}
