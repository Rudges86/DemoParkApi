package com.estacionamento.demoparkapi.config;

import com.estacionamento.demoparkapi.jwt.JwtAuthenticationEntryPoint;
import com.estacionamento.demoparkapi.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher; //Esse cara aqui que resolve o bang da request
@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class springSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //desabilitando o csrf para uma aplicação stalless com jwt
        //desabilitando o formlogin do spring
        // desabilitando o httpBasic pois não oferece segurançã nenhuma
        //aqui que vai verificar os métodos que precisam de autênticação
        //Qualquer usuário vai poder acessar para se cadastrar
        //Informa que qualquer outra requisição deve estar autênticado
        //informando qual é a sessão
        return http
                .csrf( csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antMatcher(HttpMethod.POST,"/api/v1/usuarios") ,
                                antMatcher(HttpMethod.POST, "/api/v1/auth"),
                                antMatcher("/docs/index.html"),
                                antMatcher("/docs-park.html"),
                                antMatcher("/docs-park/**"),
                                antMatcher("/v3/api-docs/**"),
                                antMatcher("/swagger-ui-custom.html"),
                                antMatcher("/swagger-ui.html"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/**.html"),
                                antMatcher("/webjars/**"),
                                antMatcher("/configuration/**"),
                                antMatcher("/swagger-resources/**")
                                ).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(
                        jwtAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling( ex -> ex
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        )
                .build();
    }

    //criptografia do passworder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Método de autênticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }
}
