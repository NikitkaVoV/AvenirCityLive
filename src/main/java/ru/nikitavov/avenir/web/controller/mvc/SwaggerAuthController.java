package ru.nikitavov.avenir.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerAuthController {

    @GetMapping("swagger")
    public String swaggerPage() {
        return "swaggerView"; // Возвращает имя HTML-шаблона (swaggerView.html)
    }
}