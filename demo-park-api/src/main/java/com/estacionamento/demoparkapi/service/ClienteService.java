package com.estacionamento.demoparkapi.service;

import com.estacionamento.demoparkapi.entity.Cliente;
import com.estacionamento.demoparkapi.exception.CpfUniqueVioletionExceiption;
import com.estacionamento.demoparkapi.exception.EntityNotFoundException;
import com.estacionamento.demoparkapi.repository.ClienteRepository;
import com.estacionamento.demoparkapi.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        //O cpf não pode ter dois iguais, vamos tratar essa exceção
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueVioletionExceiption(String.format("CPF '%s' não pode ser cadastrado, já existe no sistema", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id=%s não encontrado!", id))
        );
    }

    //Como foi implementado a paginação mudamos o tipo de lista para page
//    @Transactional(readOnly = true)
//    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
//        return clienteRepository.findAll(pageable);
//    }
//Como foi implementado a paginação mudamos o tipo de lista para page
    //Mas no final acabou criando um dto
    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
