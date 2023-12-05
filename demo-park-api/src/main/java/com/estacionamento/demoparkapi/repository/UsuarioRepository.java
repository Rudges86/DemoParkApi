package com.estacionamento.demoparkapi.repository;

import com.estacionamento.demoparkapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByUsername(String username);

    //Nesse caso por ser umn Enum vamos ser obrigados a usar query, para ele não trazer como um array e dar pau
    @Query("select u.role from Usuario u where u.username like :username")
    Usuario.Role findByRoleUsername(String username);
}
