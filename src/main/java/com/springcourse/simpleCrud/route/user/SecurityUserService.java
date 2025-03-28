package com.springcourse.simpleCrud.route.user;

import com.springcourse.simpleCrud.model.schema.UserProfile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserPrincipal(user);
    }
}
