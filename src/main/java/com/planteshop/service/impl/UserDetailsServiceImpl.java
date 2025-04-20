package com.planteshop.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.planteshop.model.entity.User;
import com.planteshop.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			return userRepository.findByEmail(email)
						 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	}
}
