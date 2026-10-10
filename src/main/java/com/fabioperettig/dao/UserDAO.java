package com.fabioperettig.dao;

import com.fabioperettig.dao.generic.GenericDAO;
import com.fabioperettig.domain.User;
import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDAO extends GenericDAO<User> {

    protected UserDAO() {
        super(User.class);
    }

    /// Metodo JPQL
    public boolean possuiAchievements(Long userId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(a) FROM Achievement a WHERE a.user.id = :userId",
                        Long.class
                )
                .setParameter("userId", userId)
                .getSingleResult();

        return count > 0;
    }
}
