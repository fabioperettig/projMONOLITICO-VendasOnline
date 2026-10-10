package com.fabioperettig.controller;


import com.fabioperettig.domain.User;
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
import java.util.List;

@Named("userController")
@ViewScoped
public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserService userService;

    @Getter
    @Setter
    private User user;

    @Getter
    private User userToDelete;

    @Getter
    private List<User> users;

    @PostConstruct
    public void init() {
        iniciarUsers();
    }

    ///Metodos CRUD
    public void create() {

        userService.create(user);
        iniciarUsers();

        showMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado.");
    }

    public void edit(User userRead) {
        User userResult = userService.read(userRead.getId());

        if (userResult == null) {
            showMessage(FacesMessage.SEVERITY_WARN, "Usuário não encontrado.");
            return;
        }

        user = userResult;
    }

    public void update() {
        if (!isEditing()) {
            showMessage(FacesMessage.SEVERITY_WARN, "Selecione um usuário para editar.");
            return;
        }

        userService.update(user);
        iniciarUsers();

        showMessage(FacesMessage.SEVERITY_INFO, "Usuário atualizado.");
    }

    public void cancel() {
        user = new User();
    }

    private void deleteUser(User user) {
        userService.delete(user);

        userToDelete = null;
        iniciarUsers();

        showMessage(FacesMessage.SEVERITY_INFO, "Usuário excluído.");
    }

    ///Auxiliares

    private void showMessage(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(severity, message, null)
        );
    }

    public boolean isEditing() {
        return user != null && user.getId() != null;
    }

    ///Instancia User e recarrega a lista
    private void iniciarUsers() {
        user = new User();
        users = userService.findAll();
    }

    public void prepareDelete(User userDelete) {
        userToDelete = null;

        User userResult = userService.read(userDelete.getId());

        if (userResult == null) {
            showMessage(FacesMessage.SEVERITY_WARN, "Usuário não encontrado.");
            return;
        }

        if (userService.possuiAchievements(userResult.getId())) {
            userToDelete = userResult;

            showMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Este usuário possui conquistas. Elas também serão excluídas."
            );
            return;
        }

        deleteUser(userResult);
    }

    public void confirmDelete() {
        if (userToDelete == null) {
            return;
        }
        deleteUser(userToDelete);
    }

    public void cancelDelete() {
        userToDelete = null;
    }
}
