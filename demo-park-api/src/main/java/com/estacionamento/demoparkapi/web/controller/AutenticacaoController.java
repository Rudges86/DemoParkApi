package com.estacionamento.demoparkapi.web.controller;

import com.estacionamento.demoparkapi.jwt.JwtToken;
import com.estacionamento.demoparkapi.jwt.JwtUserDetailService;
import com.estacionamento.demoparkapi.web.dto.UsuarioLoginDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="Autenticação", description = "Recurso para realizar a autenticação na API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AutenticacaoController {

    private final JwtUserDetailService detailService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticar o usuário", description = "Realizar a autenticação do usuário na api.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bearer token.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(responseCode = "422", description = "Campo(s) inválido(s).",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
             )

        }
    )
    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto, HttpServletRequest request) {
        try{
            log.info("Processo de autenticação pelo login {}", usuarioLoginDto.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(usuarioLoginDto.getUsername(),usuarioLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = detailService.getTokenAuthenticated(usuarioLoginDto.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (AuthenticationException ex) {
            log.warn("Erro de credencial do usuário {} ", usuarioLoginDto.getUsername());
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credênciais inválidas"));
    }
}
