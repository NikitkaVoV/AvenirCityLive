package ru.nikitavov.avenir.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig extends AsyncConfigurerSupport {
    // Дополнительные настройки, если необходимо
}