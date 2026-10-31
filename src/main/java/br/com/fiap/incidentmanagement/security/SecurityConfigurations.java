package br.com.fiap.incidentmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    // Login
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();

                    // Usuários (troca da própria senha: qualquer usuário logado)
                    req.requestMatchers(HttpMethod.PATCH, "/users/password").authenticated();
                    req.requestMatchers("/users/**").hasRole("ADMINISTRATIVO");

                    // Alunos e turmas (consulta: qualquer usuário logado; cadastro/vínculo: Administrativo)
                    req.requestMatchers(HttpMethod.GET, "/students/**", "/schoolclasses/**").authenticated();
                    req.requestMatchers("/students/**", "/schoolclasses/**").hasRole("ADMINISTRATIVO");

                    // Categorias e tipos de ocorrência
                    req.requestMatchers(HttpMethod.GET, "/incidents/categories", "/incidents/types/**").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/incidents/categories", "/incidents/types").hasRole("ADMINISTRATIVO");

                    // Dashboard e status
                    req.requestMatchers(HttpMethod.GET, "/incidents/summary").hasAnyRole("COORDENADOR", "ANALISTA");
                    req.requestMatchers(HttpMethod.PATCH, "/incidents/status").hasRole("COORDENADOR");

                    // Ocorrências (consulta: Professor, Analista e Coordenador; registro/alteração/exclusão: Professor)
                    req.requestMatchers(HttpMethod.GET, "/incidents/**").hasAnyRole("PROFESSOR", "ANALISTA", "COORDENADOR");
                    req.requestMatchers("/incidents/**").hasRole("PROFESSOR");

                    req.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
