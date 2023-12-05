package com.estacionamento.demoparkapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    //criar um bean com retorno do tipo openApi, cujo pacote deve ser do io.Swagger.v3
    @Bean
    public OpenAPI openAPI() { //aqui vai gerar o cabeçalho da página do swagger
        return new OpenAPI()
                    .info(
                            new Info()
                                    .title("REST API -  Spring park")
                                    .description("Api para gestão de estacionamento de veículos")
                                    .version("V1")
                                    .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                                    .contact(new Contact().name("Rudge Santos").email("rudges@gmail.com"))
                    );
    }
}
