package com.estacionamento.demoparkapi.web.dto.mapper;

import com.estacionamento.demoparkapi.entity.Cliente;
import com.estacionamento.demoparkapi.web.dto.ClienteCreateDto;
import com.estacionamento.demoparkapi.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

//Deixando privado para que o programador não instancie ela em qualquer lugar e por só ter métodos staticos
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}
