package ru.nikitavov.avenir.bot.general;

import ru.nikitavov.avenir.general.model.tuple.Tuple2;

import java.util.List;

public interface ISendMessageService {

    void sendMessage(List<Tuple2<List<String>, String>> messages);
}
