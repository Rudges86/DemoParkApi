package com.estacionamento.demoparkapi.web.dto;

import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class UsuarioResponseDto {
    private Long id;
    private String username;
    private String role;
}
