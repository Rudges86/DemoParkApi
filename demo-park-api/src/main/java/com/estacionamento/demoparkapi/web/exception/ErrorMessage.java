package com.estacionamento.demoparkapi.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter //Devemos adicionar o getter, pois quando é inserido no json é atráves dos get;
@ToString
public class ErrorMessage {
    //Inserir o recurso que gerou essa exceção, nesse caso o caminho exemplo "/api/v1/usuarios"
    private String path;
    //Qual foi o método que gerou está exceção exemplo "POST, GET"
    private String method;
    //Verificar o código do status exemplo 422
    private int status;
    //o texto da mensagem que é acompanhado no código de erro exemplo "Unprocessable Entity"
    private String statusText;
    //mensagem de erro que descreve o que causou o error, pode ser personalizada ou a padrão
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) //Não inclui os campos nulos no json
    //Só para mapear os erros no bindingResult, ele devolve um array de erros
    private Map<String, String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.path = request.getRequestURI(); //Retorna o caminho da request
        this.method = request.getMethod(); //Retorna o método
        this.status = status.value(); //Retorna o número do status
        this.statusText = status.getReasonPhrase(); //Retorna o texto que contém no status
        this.message = message; // retorna a mensagem
    }


    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this.path = request.getRequestURI(); //Retorna o caminho da request
        this.method = request.getMethod(); //Retorna o método
        this.status = status.value(); //Retorna o número do status
        this.statusText = status.getReasonPhrase(); //Retorna o texto que contém no status
        this.message = message; // retorna a mensagem
        addErrors(result);//Esse objeto bindingResult é um objeto que a gente tem acesso aos erros com a validação de campos
    }

    //Aqui que vai ser adicionado os erros
    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for(FieldError fieldError : result.getFieldErrors()){
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
