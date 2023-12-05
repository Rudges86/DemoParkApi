package com.estacionamento.demoparkapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioCreateDto {
    //Validação do jakarta.validator(bean validator), nesta bean de e-mail podemos adicionar a nossa regex para o e-mail
    //através do atributo regexp, Importante ressaltar que a anotação de e-mail não valida campos nulos
    //Por padrão se existir um caractere antes e depois do arroba no e-mail ele já entende como um e-mail válido
    //Para suprir este problema basta adicionar um regex
    @Email(message = "Formato do e-mail inválido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    //Se o campo estiver nullo ele não passa na validação
    //@NotNull
    //Valida se o campo é vazio ou nulo e não deixa ele passar
    @NotBlank
    //Username neste exemplo é o e-mail
    private String username;
    //Para definir a quantidade de caractere, tendo como parâmetros min e max, sendo o mínimo e máximo de caracteres
    @Size(min = 6, max = 6)
    @NotBlank
    private String password;
}
