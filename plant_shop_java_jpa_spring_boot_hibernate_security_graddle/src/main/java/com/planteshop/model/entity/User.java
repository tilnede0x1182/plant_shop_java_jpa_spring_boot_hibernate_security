package com.planteshop.model.entity;

import com.planteshop.model.enums.RoleType;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

		@NotBlank(message = "Le nom est obligatoire")
		private String name;

		@NotBlank(message = "L'email est obligatoire")
		@Email(message = "Email invalide")
		private String email;

		@NotBlank(message = "Le mot de passe est obligatoire")
		@Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
		private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "user")
    private List<CustomerOrder> orders;

		@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    /** @return Long l'identifiant unique de l'utilisateur */
    public Long getId() { return id; }
    /** @param id Long l'identifiant à définir */
    public void setId(Long id) { this.id = id; }
    /** @return String le nom de l'utilisateur */
    public String getName() { return name; }
    /** @param name String le nom à définir */
    public void setName(String name) { this.name = name; }
    /** @return String l'email de l'utilisateur */
    public String getEmail() { return email; }
    /** @param email String l'email à définir */
    public void setEmail(String email) { this.email = email; }
    /** @return String le mot de passe hashé */
    public String getPassword() { return password; }
    /** @param password String le mot de passe à définir */
    public void setPassword(String password) { this.password = password; }
    /** @return RoleType le rôle de l'utilisateur */
    public RoleType getRole() { return role; }
    /** @param role RoleType le rôle à définir */
    public void setRole(RoleType role) { this.role = role; }
    /** @return List la liste des commandes de l'utilisateur */
    public List<CustomerOrder> getOrders() { return orders; }
    /** @param orders List la liste des commandes à définir */
    public void setOrders(List<CustomerOrder> orders) { this.orders = orders; }
    /** @return String l'email utilisé comme username Spring Security */
    @Override public String getUsername() { return email; }
    /** @return boolean true car le compte n'expire jamais */
    @Override public boolean isAccountNonExpired() { return true; }
    /** @return boolean true car le compte n'est jamais verrouillé */
    @Override public boolean isAccountNonLocked() { return true; }
    /** @return boolean true car les credentials n'expirent jamais */
    @Override public boolean isCredentialsNonExpired() { return true; }
    /** @return boolean true car le compte est toujours actif */
    @Override public boolean isEnabled() { return true; }
}
