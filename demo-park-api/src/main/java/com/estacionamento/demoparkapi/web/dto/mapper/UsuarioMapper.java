package com.estacionamento.demoparkapi.web.dto.mapper;

import com.estacionamento.demoparkapi.entity.Usuario;
import com.estacionamento.demoparkapi.web.dto.UsuarioCreateDto;
import com.estacionamento.demoparkapi.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
//converte de usuário dto para usuario nesse caso aqui foi utilizado para cadastrar
    public static Usuario toUsuario(UsuarioCreateDto createDto) {
        //primeiro argumento é a classe a ser convertida ( a fonte) o segundo argumento é o destino
        return new ModelMapper().map(createDto, Usuario.class);
    }

    //converte de usuario para usuarioResponseDto para devolver
    public static UsuarioResponseDto toDto(Usuario usuario) {
        //Convertendo o enum para sting para remover o underline
        String role = usuario.getRole().name().substring("ROLE_".length());
        //Agora vamos pegar o valor de role e inserir direto na classe dto, é preciso passar um genérico entre classe fonte e destino, sendo o primeiro fonte.
        PropertyMap<Usuario,UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            //Neste método é feito a configuração, neste caso setando a role que criamos acima
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }
    //usado para listar todos

    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios) {
        //vamos pegar cada um desses objetos da lista e adicionar em uma lista do tipo usuário response dto
        return usuarios.stream().map(user -> toDto(user)).collect(Collectors.toList()); //Com o stream o map vai passar por todos os objetos de usuário
        //e no final vai devolver o toDto como lista
    }
}
