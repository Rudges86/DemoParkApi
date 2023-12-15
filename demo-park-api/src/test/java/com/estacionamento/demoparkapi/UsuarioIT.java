package com.estacionamento.demoparkapi;

import com.estacionamento.demoparkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioSenhaDto;
import com.estacionamento.demoparkapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

//Primeira classe de teste
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT ) //Anotação de teste, a primeira configuração para fazer o spring executar o tomcat
                                   // de teste em forma randômica
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //anotação para importar os scripts do sql que foi criado
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired //Injeta o cliente de teste
    WebTestClient testClient;

    //O próximo passo é criar um diretório resource no diretório de testes, e adicionar um novo arquivo properties chamado
    //application para fazer as configurações de properties para testes

    //Realizando o primeiro teste
    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
         //o motivo do teste o que vai ser testado e o retorno do teste
    public void createUsuario_WithUsernameAndPasswordValid_ReturnUserCreateStat201() {
        UsuarioResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.com","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isCreated()//é o que esperamos no status da requisição
                .expectBody(UsuarioResponseDto.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_WithUsernameInvalid_ReturnErrorMessage422() {
        //Aqui montamos o objeto para teste
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        //Outra possibilidade de teste
        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        //Outra possibilidade de teste
        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@email.","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }


    @Test
    public void createUsuario_WithUserPasswordInvalid_ReturnErrorMessage422() {
        //Aqui montamos o objeto para teste
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com","12356"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        //Outra possibilidade de teste
        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com",""))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        //Outra possibilidade de teste
        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("tody@gmail.com","1234567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        //Aqui que testamos se bate o esperado
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void createUsuario_WithRepeatUsername_ReturnUserCreateStat201() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(409)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }


    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void findUser_WithIdExist_ReturnStatus200() {
        //Admin buscando seus dados
        UsuarioResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //aqui é para o token
                //.contentType(MediaType.APPLICATION_JSON) é removido, pois não estou enviando um json só fazendo uma busca
                //.bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))  -> não vai dados na requisição por isso ele é removido
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isOk()//é o que esperamos no status da requisição
                .expectBody(UsuarioResponseDto.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

//Admin buscando outro cliente
        responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //aqui é para o token
                //.contentType(MediaType.APPLICATION_JSON) é removido, pois não estou enviando um json só fazendo uma busca
                //.bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))  -> não vai dados na requisição por isso ele é removido
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isOk()//é o que esperamos no status da requisição
                .expectBody(UsuarioResponseDto.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar uma classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("maria@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

        //Cliente buscando seus próprios dados
        responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456")) //aqui é para o token
                //.contentType(MediaType.APPLICATION_JSON) é removido, pois não estou enviando um json só fazendo uma busca
                //.bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))  -> não vai dados na requisição por isso ele é removido
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isOk()//é o que esperamos no status da requisição
                .expectBody(UsuarioResponseDto.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("maria@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }


    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void findUser_WithIdDoesNotExist_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/2")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                //.contentType(MediaType.APPLICATION_JSON) é removido, pois não estou enviando um json só fazendo uma busca
                //.bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))  -> não vai dados na requisição por isso ele é removido
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isNotFound()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void findUser_WithUserClientFindOtherUser_ReturnStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/2")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456")) //Para o jwt
                //.contentType(MediaType.APPLICATION_JSON) é removido, pois não estou enviando um json só fazendo uma busca
                //.bodyValue(new UsuarioCreateDto("ana@gmail.com","123456"))  -> não vai dados na requisição por isso ele é removido
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isForbidden()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void editPassword_WithValidData_ReturnStatus204() {
        //UsuarioSenhaDto responseBody = testClient -> por não ter retorno não precisamos botar em um objeto
         testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456","123567","123567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isNoContent();//é o que esperamos no status da requisição
//                .expectBody(UsuarioSenhaDto.class) //é o que esperamos no corpo da requisição -> nesse caso não vamos ter um corpo de resposta
                //.returnResult().getResponseBody(); //é o que espera retornar na requisição -> não vamos ter nessa requisição por não ter um corpo

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        /* Por não ter resposta não precisa testar o objeto de retorno
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getSenhaAtual()).isEqualTo("123456");
        org.assertj.core.api.Assertions.assertThat(responseBody.getNovaSenha()).isEqualTo("123567");
        org.assertj.core.api.Assertions.assertThat(responseBody.getConfirmaSenha()).isEqualTo("123567");
        */


        //Editando a senha do usuário
        testClient
                .patch()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456","123567","123567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isNoContent();
    }

  /* Removido por conta do PreAuthorize e substituido pelo 403
    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void editPassword_WithIdDoesNotExist_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("123456","123567","123567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isNotFound()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }*/

    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void editPassword_WithIdDoesNotExistWithDifferentUsers_ReturnStatus403() {
        //teste com admin
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("123456","123567","123567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isForbidden()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        //teste com cliente
        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("123456","123567","123567"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isForbidden()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void editPassword_WithInvalidFields_ReturnStatus422() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("","",""))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("12345","12345","12345"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("1234578","1234578","1234578"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(422)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void editPassword_WithInvalidPasswords_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("000000","123456","123456"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(400)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .contentType(MediaType.APPLICATION_JSON) //é removido, pois não estou enviando um json só fazendo uma busca
                .bodyValue(new UsuarioSenhaDto("123456","123456","000000"))
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isEqualTo(400)//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);


    }

    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void findAllUsers_ReturnStatus200() {
        List<UsuarioResponseDto> responseBody = testClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com","123456")) //Para o jwt
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isOk()//é o que esperamos no status da requisição
                .expectBodyList(UsuarioResponseDto.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);

    }



    @Test //Anotação para testar, são sempre público e sem retorno os testes. No teste é importante que tenha:
    //o motivo do teste o que vai ser testado e o retorno do teste
    public void findAllUsers_WithUserWithoutPermission_ReturnStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@gmail.com","123456")) //Para o jwt
                .exchange() //é o que é esperado por nós após a requisição
                .expectStatus().isForbidden()//é o que esperamos no status da requisição
                .expectBody(ErrorMessage.class) //é o que esperamos no corpo da requisição
                .returnResult().getResponseBody(); //é o que espera retornar na requisição

        //vamos utilizar una classe chamada assetion com o seguinte pacote org.assertj.core.api.Assertions que da os métodos de teste para o objeto
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }
}
