package com.estacionamento.demoparkapi.web.controller;

import com.estacionamento.demoparkapi.entity.Cliente;
import com.estacionamento.demoparkapi.jwt.JwtUserDetails;
import com.estacionamento.demoparkapi.repository.projection.ClienteProjection;
import com.estacionamento.demoparkapi.service.ClienteService;
import com.estacionamento.demoparkapi.service.UsuarioService;
import com.estacionamento.demoparkapi.web.dto.ClienteCreateDto;
import com.estacionamento.demoparkapi.web.dto.ClienteResponseDto;
import com.estacionamento.demoparkapi.web.dto.PageableDTO;
import com.estacionamento.demoparkapi.web.dto.mapper.ClienteMapper;
import com.estacionamento.demoparkapi.web.dto.mapper.PageableMapper;
import com.estacionamento.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
                           "Requisição exige o uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name="security"),
                    responses = {
                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                        content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                        @ApiResponse(responseCode = "409", description = "Cliente já possui cpf cadastrado no sistema",
                                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil admin",
                                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    }
    )


    //Como temos o relacionamento 1 para 1 vamos precisar vincular na hora de salvar o cliente no banco de dados
    //Para que seja feito o relacionamento, para isso vamos pegar o userDetail o id do usuário
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorID(userDetails.getId()));
        clienteService.salvar(cliente);

        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));


    }

    @Operation(summary = "Buscar um cliente pelo id", description = "Recurso para buscar um cliente pelo id. " +
            "Requisição exige o uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name="security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }


    @Operation(summary = "Buscar todos os clientes pelo  com paginação", description = "Requisição exige o uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name="security"),
            parameters = {
                    @Parameter(in = QUERY, name="page",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                        description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name="size",
                         content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                         description = "Representa o total de elementos por página."
                    ),
//o hidden vai fazer o swager esconder o valor quando formos fazer a requisição evitando problema por não saber manipular a string no objeto pageable
                    //Para isso funcionar passamos a mesma anotation @Parameter como argumento do getAll
                    @Parameter(in = QUERY, name="sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a ordenação dos resultados. Aceita multiplos critérios de ordenação são suportados."
                    )
                },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),

                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )

    //Implementando uma paginação basta usar o pageable, ele é do pacote org.springframewor.data.domain
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    //@PageableDefault serve para mudarmos os valores padrões do pageable, como tamanho valores direções
    public ResponseEntity<PageableDTO> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
            Page<ClienteProjection> clientes = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsuarioId(userDetails.getId()); //Fazendo um join na busca
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
