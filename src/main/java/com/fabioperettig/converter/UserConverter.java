package com.fabioperettig.converter;

import com.fabioperettig.domain.User;
import jakarta.faces.component.UIComponent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

import java.util.HashMap;
import java.util.Map;

@FacesConverter(value = "userConverter", forClass = User.class)
public class UserConverter implements Converter<User> {

    private static final String KEY = UserConverter.class.getName();

    /**
     * <p>
     * <span class="changed_modified_2_3">Convert</span> the specified string value, which is associated with the specified
     * {@link UIComponent}, into a model data object that is appropriate for being stored during the
     * <em class="changed_modified_2_3">Process Validations</em> phase of the request processing lifecycle.
     * </p>
     *
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object value is associated
     * @param value     String value to be converted (may be <code>null</code>)
     * @return <code>null</code> if the value to convert is <code>null</code>, otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully performed
     * @throws NullPointerException if <code>context</code> or <code>component</code> is <code>null</code>
     */
    @Override
    public User getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        User user = getUsersMap(context).get(value);

        if (user == null) {
            throw new ConverterException(
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Usuário inválido.",
                            "Selecione um usuário da lista."
                    )
            );
        }
        return user;
    }

    /**
     * <p>
     * Convert the specified model object value, which is associated with the specified {@link UIComponent}, into a String
     * that is suitable for being included in the response generated during the <em>Render Response</em> phase of the
     * request processing lifeycle.
     * </p>
     *
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object value is associated
     * @param value     Model object value to be converted (may be <code>null</code>)
     * @return a zero-length String if value is <code>null</code>, otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully performed
     * @throws NullPointerException if <code>context</code> or <code>component</code> is <code>null</code>
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, User value) {

        if (value == null || value.getId() == null) {
            return "";
        }

        String id = value.getId().toString();
        getUsersMap(context).put(id, value);

        return id;
    }

    @SuppressWarnings("unchecked")
    private Map<String, User> getUsersMap(FacesContext context) {
        Map<String, Object> viewMap =
                context.getViewRoot().getViewMap();

        Map<String, User> usersMap =
                (Map<String, User>) viewMap.get(KEY);

        if (usersMap == null) {
            usersMap = new HashMap<>();
            viewMap.put(KEY, usersMap);
        }

        return usersMap;
    }
}
