package com.fabioperettig.service;

import com.fabioperettig.dao.AchievementDAO;
import com.fabioperettig.domain.Achievement;
import com.fabioperettig.dao.UserDAO;
import com.fabioperettig.domain.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class AchievementService {

    @Inject
    private AchievementDAO achievementDAO;

    @Inject
    private UserDAO userDAO;


    public Achievement create(Achievement achievement) {
        associateExistingUser(achievement);
        return achievementDAO.create(achievement);
    }

    public Achievement read(Long id) {
        return achievementDAO.read(id);
    }

    public Achievement update(Achievement achievement) {
        associateExistingUser(achievement);
        return achievementDAO.update(achievement);
    }

    public void delete(Achievement achievement) {
        achievementDAO.delete(achievement);
    }

    public List<Achievement> findAll() {
        return achievementDAO.findAll();
    }


    /// Associa a conquista a um usuário já cadastrado
    private void associateExistingUser(Achievement achievement) {
        if (achievement == null) {
            throw new IllegalArgumentException("Informe a conquista.");
        }

        User selectedUser = achievement.getUser();

        if (selectedUser == null || selectedUser.getId() == null) {
            throw new IllegalArgumentException(
                    "Selecione um usuário já cadastrado."
            );
        }

        User existingUser = userDAO.read(selectedUser.getId());

        if (existingUser == null) {
            throw new IllegalArgumentException(
                    "O usuário selecionado não existe."
            );
        }

        achievement.setUser(existingUser);
    }
}
