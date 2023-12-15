package com.estacionamento.demoparkapi.repository;

import com.estacionamento.demoparkapi.entity.Cliente;
import com.estacionamento.demoparkapi.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    //Ao criar este método em específico, vamos ter que fazer a consulta via query
    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable1);

    Cliente findByUsuarioId(Long id);
}
