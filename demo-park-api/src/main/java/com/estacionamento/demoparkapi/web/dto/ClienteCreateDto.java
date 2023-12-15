package com.estacionamento.demoparkapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteCreateDto {

    @NotBlank
    @Size(max = 100, min = 5)
    private String nome;

    @NotBlank
    @Size(max =11 , min = 11)
    @CPF
    private String cpf;

}
