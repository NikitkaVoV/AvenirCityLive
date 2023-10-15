//package ru.nikitavov.kkep.bot.telegram;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.nikitavov.kkep.database.model.base.LinkedSocialNetwork;
//import ru.nikitavov.kkep.database.repository.realisation.LinkedSocialNetworkRepository;
//import ru.nikitavov.kkep.general.model.socuialnetwork.SocialNetworkType;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@CrossOrigin
//@RestController
//@RequestMapping("bot/telegram")
//public class TelegramController {
//
//    private final TelegramSendMessageService messageService;
//    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
//
//    @PostMapping("/message/send")
//    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestVk message) {
//
//        Optional<LinkedSocialNetwork> socialNetwork = linkedSocialNetworkRepository.
//        findByUserSocialNetworkIdAndSocialNetworkType(Integer.toString(message.id), SocialNetworkType.TELEGRAM);
//
//        if (socialNetwork.isEmpty()) return ResponseEntity.notFound().build();
//
//        messageService.sendMessage(socialNetwork.get(), message.message);
//
//        return ResponseEntity.ok().build();
//    }
//
//    public record MessageRequestVk(int id, String message) {
//
//    }
//}
