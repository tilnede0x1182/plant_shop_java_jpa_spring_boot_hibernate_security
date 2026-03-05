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

    /**
     * Constructeur avec injection du repository.
     *
     * @param userRepository UserRepository le repository des utilisateurs
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

		/**
		 * Charge un utilisateur par son email pour l'authentification Spring Security.
		 *
		 * @param email String l'email de l'utilisateur
		 * @return UserDetails les details de l'utilisateur
		 * @throws UsernameNotFoundException si utilisateur non trouve
		 */
		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			return userRepository.findByEmail(email)
						 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	}
}
