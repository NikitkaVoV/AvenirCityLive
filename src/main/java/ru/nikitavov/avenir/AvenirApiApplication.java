package ru.nikitavov.avenir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.nikitavov.avenir.bot.BotProperties;
import ru.nikitavov.avenir.general.localization.I18n;
import ru.nikitavov.avenir.general.points.RegionService;
import ru.nikitavov.avenir.web.security.AppProperties;

@SpringBootApplication()
@EnableConfigurationProperties({AppProperties.class, BotProperties.class})
public class AvenirApiApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(AvenirApiApplication.class, args);
        I18n.init();

        context.getBean(RegionService.class).parseFile();
    }
}
