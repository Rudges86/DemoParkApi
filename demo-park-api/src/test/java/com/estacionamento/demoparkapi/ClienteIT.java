package com.estacionamento.demoparkapi;

import com.estacionamento.demoparkapi.web.dto.ClienteCreateDto;
import com.estacionamento.demoparkapi.web.dto.ClienteResponseDto;
import com.estacionamento.demoparkapi.web.dto.PageableDTO;
import com.estacionamento.demoparkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT ) //Anotação de teste, a primeira configuração para fazer o spring executar o tomcat
// de teste em forma randômica
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //anotação para importar os scripts do sql que foi criado
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {
    @Autowired
    WebTestClient testClient;

    @Test
    public void criarUmcliente_ComDadosValidos_RetornarClienteComStatus201() {
        ClienteResponseDto responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "83612015052"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getNome()).isEqualTo("Tobias Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getCpf()).isEqualTo("83612015052");

    }



    @Test
    public void criarUmcliente_ComCPFJaCadastrado_RetornarErrorMessageStatus409() {
        ErrorMessage responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "46430999092"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(409);

    }


    @Test
    public void criarUmcliente_ComDadosInvalidos_RetornarErrorMessageStatus422() {
        ErrorMessage responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(422);

        responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("Bob", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(422);


        responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "marcos@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("", "836.120.150-52"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(422);
    }





    @Test
    public void criarUmcliente_ComUsuarioNaoPermitido_RetornarErrorMessageStatus403() {
        ErrorMessage responseBodyOk =  testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456"))
                .bodyValue(new ClienteCreateDto("Tobias Ferreira", "83612015052"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(403);


    }


    @Test
    public void buscarUmcliente_ComIdExistentePeloAmdin_RetornarErrorMessageStatus200() {
        ClienteResponseDto responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getId()).isEqualTo(10);
    }

    @Test
    public void buscarUmcliente_ComIdInexistentePeloAmdin_RetornarErrorMessageStatus404() {
        ErrorMessage responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarUmcliente_ComIdExistentePeloCliente_RetornarErrorMessageStatus403() {
        ErrorMessage responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes/11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloAdmin_RetornarClienteStatus200() {
        PageableDTO responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getContent().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getTotalPages()).isEqualTo(1);



        responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloAdmin_RetornarErroMessageComStatus403() {
        ErrorMessage responseBodyOk =  testClient
                .get()
                .uri("/api/v1/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBodyOk).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBodyOk.getStatus()).isEqualTo(403);
    }


}
