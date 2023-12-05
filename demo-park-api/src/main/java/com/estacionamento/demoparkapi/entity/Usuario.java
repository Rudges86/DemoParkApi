package com.estacionamento.demoparkapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="usuarios")
@Getter @Setter @NoArgsConstructor
//não é uma boa prática utilizar o @Data, por conta dele gerar equals e hashcodes para todos os campos, e só é preciso gerar para o campo de id
@EntityListeners(AuditingEntityListener.class) //Anotation para habilitar o sistema de auditoria
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Serve para mapeamento e criação das colunas na tabela no banco
    @Column(name="id")
    private Long id;

    @Column(name="username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name="password", nullable = false, length= 200)
    private String password;

    //Perfis de autenticação
    @Enumerated(EnumType.STRING) //Transformando o enum em uma string para salvar no banco de dados
    @Column(name="role", nullable = false, length = 25)
    //Como é um consenso que qualquer usuário que crie o seu cadastro já venha como client, logo a variável já vai se iniciar como cliente
    //Caso seja necessário mudar para a admin a troca será feita posteriormente
    private Role role = Role.ROLE_CLIENTE;

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

    //O enum de perfis que normalmente ficariam em uma classe separada, ele criou na mesma classe
    public enum Role {
        ROLE_ADMIN, ROLE_CLIENTE

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                '}';
    }
}
