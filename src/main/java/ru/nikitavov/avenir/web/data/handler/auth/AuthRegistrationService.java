package ru.nikitavov.avenir.web.data.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.nikitavov.avenir.web.message.model.responsemessage.ResponseMessageFactory;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.service.ResponseConverterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class AuthRegistrationService {

    public static String PARAMETER_REGISTRATION = "reg";
    public static String PARAMETER_REGISTRATION_COOKIE = "user_reg";
    public static String PARAMETER_NAME = "name";
    public static String PARAMETER_NAME_COOKIE = "user_name";
    public static String PARAMETER_SURNAME = "surname";
    public static String PARAMETER_SURNAME_COOKIE = "user_surname";
    public static String PARAMETER_PATRONYMIC = "patronymic";
    public static String PARAMETER_PATRONYMIC_COOKIE = "user_patronymic";

    private final ResponseConverterService responseConverterService;

    public boolean registrationOAuth2(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    public boolean validationRegistrationOAuth2(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter(PARAMETER_NAME).trim();
        String surname = request.getParameter(PARAMETER_NAME).trim();

        return checkParameter(request, response, name, "name", "entity.registration.name")
                && checkParameter(request, response, surname, "surname", "entity.registration.surname");
    }

    public boolean checkParameter(HttpServletRequest request, HttpServletResponse response, String param, String paramName, String langParamName) {
        if (StringUtils.hasText(param)) return true;

        MessageWrapper<Object> messageWrapper = MessageWrapper.create(null,
                ResponseMessageFactory.createMessageFieldLocalized(paramName,
                        langParamName,
                        "message.warn.field.empty")
        );

        responseConverterService.writeMessageWrapperInResponseParamAndRedirect(request, response, messageWrapper, "response");

//        RedirectUrl
        return false;
    }

    public boolean isRegistrationRequest(HttpServletRequest request) {
        String reg = request.getParameter(PARAMETER_REGISTRATION);
        if (StringUtils.hasText(reg)) {
            try {
                return Boolean.parseBoolean(reg);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
