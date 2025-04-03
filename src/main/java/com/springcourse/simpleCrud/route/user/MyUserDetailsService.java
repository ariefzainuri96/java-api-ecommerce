package com.springcourse.simpleCrud.route.user;

import com.springcourse.simpleCrud.model.schema.UserProfile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("Email or password is invalid!");
        }

        return new UserPrincipal(user);
    }
}
