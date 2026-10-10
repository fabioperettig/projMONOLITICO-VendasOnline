package com.fabioperettig.controller;

import com.fabioperettig.domain.Achievement;
import com.fabioperettig.domain.User;
import com.fabioperettig.service.AchievementService;
import com.fabioperettig.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named("achievementController")
@ViewScoped
public class AchievementController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AchievementService achievementService;

    @Inject
    private UserService userService;

    @Getter
    @Setter
    private Achievement achievement;

    @Getter
    private List<Achievement> achievements;

    @Getter
    private List<User> users;

    @PostConstruct
    public void init() {
        iniciarAchievements();
        users = userService.findAll();
    }

    ///Metodos CRUD

    public void create() {
        achievementService.create(achievement);
        iniciarAchievements();

        showMessage(FacesMessage.SEVERITY_INFO, "Conquista cadastrada.");
    }

    public void edit(Achievement achievementRead) {
        Achievement achievementResult =
                achievementService.read(achievementRead.getId());

        if (achievementResult == null) {
            showMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Conquista não encontrada."
            );
            return;
        }

        achievement = achievementResult;
    }

    public void update() {
        if (!isEditing()) {
            showMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Selecione uma conquista para editar."
            );
            return;
        }

        achievementService.update(achievement);
        iniciarAchievements();

        showMessage(FacesMessage.SEVERITY_INFO, "Conquista atualizada.");
    }

    public void cancel() {
        achievement = novoAchievement();
    }

    public void delete(Achievement achievementDelete) {
        achievementService.delete(achievementDelete);
        iniciarAchievements();

        showMessage(FacesMessage.SEVERITY_INFO, "Conquista excluída.");
    }

    ///Auxiliares

    private void showMessage(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(severity, message, null)
        );
    }

    public boolean isEditing(){
        return achievement != null && achievement.getId() != null;
    }

    ///Instancia Achievement e recarrega a lista
    private void iniciarAchievements() {
        achievement = novoAchievement();
        achievements = achievementService.findAll();
    }

    private Achievement novoAchievement() {
        Achievement achievement = new Achievement();
        achievement.setDataConquista(LocalDate.now());
        achievement.setPrivado(false);

        return achievement;
    }
}
