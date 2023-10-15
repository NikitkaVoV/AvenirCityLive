package ru.nikitavov.avenir.web.security.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.nikitavov.avenir.web.security.data.PermissionAuthorize;
import ru.nikitavov.avenir.web.security.data.JwtAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class PermissionAuthorizeInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PermissionAuthorize annotation = null;
        if (handler instanceof HandlerMethod handlerMethod) {
            annotation = handlerMethod.getMethod().getAnnotation(PermissionAuthorize.class);
            if (annotation == null) {
                annotation = handlerMethod.getBean().getClass().getAnnotation(PermissionAuthorize.class);
            }
        }

        if (annotation != null) {
            String functionName = annotation.value().getPermission();
            boolean hasFunction = hasFunctionPermission(functionName);
            if (!hasFunction) {
                throw new AccessDeniedException("Insufficient permissions");
            }
        }

        return true;
    }

    private boolean hasFunctionPermission(String permissionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getUserPrincipal().getPermissions().contains(permissionName);
        }

        return false;
    }
}
