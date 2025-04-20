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

    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 		System.err.println("⚙️ Méthode loadUserByUsername appelée");
    //     System.err.println("🔍 Tentative de connexion avec : " + email);
    //     User user = userRepository.findByEmail(email)
    //         .orElseThrow(() -> {
    //             System.err.println("Email introuvable en base : " + email);
    //             return new UsernameNotFoundException("Utilisateur non trouvé");
    //         });

		// 		System.err.println("DEBUG : Utilisateur trouvé : " + user.getEmail());
		// 		System.err.println("DEBUG : Hash stocké : " + user.getPassword());
		// 		return org.springframework.security.core.userdetails.User
		// 			.withUsername(user.getEmail())
		// 			.password(user.getPassword())
		// 			.roles(user.getRole().name())
		// 			.build();

    // }

		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			return userRepository.findByEmail(email)
						 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	}
}
