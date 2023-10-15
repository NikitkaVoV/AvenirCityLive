package ru.nikitavov.avenir.web.security.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.exception.ResourceNotFoundException;

import java.util.UUID;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPrincipalService principalService;
    
    @Override
    public UserPrincipal loadUserByUsername(String uuid)
            throws UsernameNotFoundException {
        User user = userRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with uuid : " + uuid));

        return principalService.create(user);
    }

    public UserPrincipal loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return principalService.create(user);
    }
}