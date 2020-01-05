package com.example.blogger.services;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = this.userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new UserPrincipal(foundUser);
    }
}
