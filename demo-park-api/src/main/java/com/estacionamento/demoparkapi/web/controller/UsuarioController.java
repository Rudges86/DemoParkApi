package com.estacionamento.demoparkapi.web.controller;

import com.estacionamento.demoparkapi.entity.Usuario;
import com.estacionamento.demoparkapi.service.UsuarioService;
import com.estacionamento.demoparkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioResponseDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioSenhaDto;
import com.estacionamento.demoparkapi.web.dto.mapper.UsuarioMapper;
import com.estacionamento.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name="Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.") //Anotação do swagger para documentação
@RequiredArgsConstructor
@RestController //Indica que é tipo rest suas requisições
@RequestMapping("api/v1/usuarios") //Configuração do path de acesso aos recursos o caminho base de acesso
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário", description = "Recuso para criar um novo usuário.", responses= {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "409",description = "Usuário e-mail já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422",description = "Campos inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    }) //Anotation da documentação, vai dizer o que a operação faz
    //O response enttiy serve para encapsular o usuário em um json e junto com o objeto usuário ele guardar mais informações como resposta e cabeçalho de resposta
    @PostMapping
    //Aqui no argumento para o bean validator funcionar devemos passar mais uma anotação como argumento que é o @Valid
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
        //aqui vamos fazer a conversão do dto para o usuario na camada do controller, neste caso vou está utilizando a biblioteca do modelmap
        //dentro do pacote dto, vamos criar um pacote chamado mapper
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        //Após enviar para o service ele vai ter que devolver um outro usuário dto de resposta, logo vamos ter que converter mais uma vez
        //Então vamos puxar o mapper para fazer a conversão
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Recuperar um usuário pelo id", description = "Recuso para recuperar um usuário pelo id.", responses= {
            @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") //É uma anotação utilizada para adicionar as nossas regras de permissões de acesso, o hasRole é a parte de segurança para dar acesso ao perfil
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorID(id);

        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));
    }


    @Operation(summary = "Atualizar senha", description = "Atualizar senha.", responses= {
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400",description = "Senha não confere.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422",description = "Campos inválidos ou não formatados.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PatchMapping("/{id}")//Devolvendo um valor vazio só passando o void dentro do responseEntity
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar todos os usuários.", description = "Buscar todos os usuários.", responses= {
            @ApiResponse(responseCode = "200", description = "Todos os usuários ok.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toListDto(users));
    }
}
