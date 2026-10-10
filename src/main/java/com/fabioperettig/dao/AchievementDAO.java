package com.fabioperettig.dao;

import com.fabioperettig.dao.generic.GenericDAO;
import com.fabioperettig.domain.Achievement;
import jakarta.enterprise.context.Dependent;

@Dependent
public class AchievementDAO extends GenericDAO<Achievement> {

    protected AchievementDAO() {
        super(Achievement.class);
    }
}
