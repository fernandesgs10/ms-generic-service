package br.com.generic.service.config;

import br.com.generic.service.entity.RoleEntity;
import br.com.generic.service.entity.UserEntity;
import br.com.generic.service.repository.AuthenticationRepository;
import br.com.generic.service.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class JwtConfig {


    private final AuthenticationRepository authenticationRepository;
    private final RoleRepository roleRepository;


    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            // Verifica se o usuário existe
            Optional<UserEntity> optionalUser = authenticationRepository.findByEmailWithRoles(email);
            UserEntity user = optionalUser.orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email: " + email)
            );

            // Verifica a role do usuário
            Optional<RoleEntity> role = roleRepository.findByRoleName("ADMIN");
            boolean hasAdminRole = user.getRoles().stream()
                    .anyMatch(roleMongo -> roleMongo.getRoleName().equals(role.map(RoleEntity::getRoleName).orElse("")));

            // Cria o UserDetails com base na role
            if (hasAdminRole) {
                return User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(AuthorityUtils.createAuthorityList("ROLE_" + role.get().getRoleName()))
                        .build();
            } else {
                // Retorna um usuário padrão se não tiver a role ADMIN
                return User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                        .build();
            }

        };

    }

    private UserDetails createFakeUser() {
        return User.builder()
                .username("fakeuser@example.com")
                .password("{noop}fakepassword")
                .roles("USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(true)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
