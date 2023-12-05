package com.estacionamento.demoparkapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration //Configuração para pegar as modificações feitas por usuário como sistema de auditoria
public class SpringJpaAuditingConfig implements AuditorAware <String>{
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Pegando o nome do usuário logado
        if(authentication != null && authentication.isAuthenticated()) { //Para evitar um null point, e precisa ter alguém autenticado para pegar o nome do usuário
            return Optional.of(authentication.getName());
        }
        return null;
    }
}
