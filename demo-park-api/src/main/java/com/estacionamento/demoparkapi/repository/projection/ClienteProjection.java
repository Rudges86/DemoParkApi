package com.estacionamento.demoparkapi.repository.projection;

//Interface criada para projetar os dados que queremos na paginação, substituindo um dto
public interface ClienteProjection {

    Long getId();
    String getNome();
    String getCpf();

}
