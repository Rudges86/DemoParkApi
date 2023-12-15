package com.estacionamento.demoparkapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.internal.asm.commons.SerialVersionUIDAdder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clientes")
@EntityListeners(AuditingEntityListener.class)
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nome", nullable = false, length = 100)
    private String nome;
    @Column(name="cpf", nullable = false, length = 11, unique = true)
    private String cpf;
    //Relacionamento 1 para 1
    @OneToOne
    //Faz o mapeamento entre as duas tabelas, name é a chave estrageira na tabela
    @JoinColumn(name ="id_usuario", nullable = false)
    private Usuario usuario;

    //Sistema de auditoria feito como solicitado no desafio
    @CreatedDate //Anotation do processo de auditoria
    @Column(name="data_criacao")
    private LocalDateTime dataCriacao;

    @LastModifiedDate //Anotation do processo de auditoria
    @Column(name="data_modificacao")
    private LocalDateTime dataModificacao;

    @CreatedBy //Anotation do processo de auditoria
    @Column(name="criado_por")
    private String criadoPor;

    @LastModifiedBy //Anotation do processo de auditoria
    @Column(name="modificado_por")
    private String modificadoPor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
