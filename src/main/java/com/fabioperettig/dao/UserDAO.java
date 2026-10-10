package com.fabioperettig.dao;

import com.fabioperettig.dao.generic.GenericDAO;
import com.fabioperettig.domain.User;
import jakarta.enterprise.context.Dependent;

@Dependent
public class UserDAO extends GenericDAO<User> {

    protected UserDAO() {
        super(User.class);
    }
}
