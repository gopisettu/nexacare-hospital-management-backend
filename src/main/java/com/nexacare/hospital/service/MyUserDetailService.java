package com.nexacare.hospital.service;

import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j

public class MyUserDetailService implements UserDetailsService {
    private  final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Loading user details for username '{}'.", username);

        return userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("User name NotFound"));
    }
}
