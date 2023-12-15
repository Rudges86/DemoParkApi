package com.estacionamento.demoparkapi;

import com.estacionamento.demoparkapi.jwt.JwtToken;
import com.estacionamento.demoparkapi.web.dto.UsuarioLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

//Classe criada para refatorar os testes agora que temos o spring security funcionando
public class JwtAuthentication {
    //a classe vai pegar o token para que a gente sete nas requisições

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        //Recupera o token para o teste
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
