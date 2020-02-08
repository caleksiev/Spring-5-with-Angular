package com.fmi.spring5.service;

import com.fmi.spring5.model.User;
import com.fmi.spring5.model.UserPrincipal;
import com.fmi.spring5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImp(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User foundUser = this.userRepository.findUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new UserPrincipal(foundUser);
    }
}
