package com.estacionamento.demoparkapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

//A anotation aqui informa que é uma classe de configuração, garantindo que quando o projeto for rodado ele vai verificar e adicionar as configurações dessa classe
@Configuration
public class SpringTimezoneConfig {
    //Essa anotation faz com que após a classe ser inicializada pelo spring o método constroturo dela seja executado. Após a execução do construtor ele
    //irá executar o timezoneConfig
    @PostConstruct
    public void timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
