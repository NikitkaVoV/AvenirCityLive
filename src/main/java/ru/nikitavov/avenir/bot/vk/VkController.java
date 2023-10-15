package ru.nikitavov.avenir.bot.vk;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.database.repository.realisation.LinkedSocialNetworkRepository;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;

import java.util.Optional;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("bot/vk")
public class VkController {

    private final VkSendMessageService messageService;
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;

    @PostMapping("/message/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestVk message) throws Exception {

        Optional<LinkedSocialNetwork> socialNetwork = linkedSocialNetworkRepository.
        findByUserSocialNetworkIdAndSocialNetworkType(Integer.toString(message.id), SocialNetworkType.VK);

        if (socialNetwork.isEmpty()) return ResponseEntity.notFound().build();

//        messageService.sendMessage(socialNetwork.get(), message.message);

        return ResponseEntity.ok().build();
    }

    public record MessageRequestVk(int id, String message) {

    }
}
