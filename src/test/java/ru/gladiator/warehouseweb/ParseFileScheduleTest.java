package ru.gladiator.warehouseweb;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

public class ParseFileScheduleTest {

    @Test
    public void testReplacementLessonParseOneLesson() {
        Assert.isTrue(LessonFileParser.newLessonNotEmpty("Zamena_na_11_05_2023.xaml", 1));
    }
    @Test
    public void testReplacementLessonParseRemove() {
        Assert.isTrue(LessonFileParser.newLessonEmpty("Zamena_na_11_05_2023.xaml", 2));
    }
}


