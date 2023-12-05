package com.estacionamento.demoparkapi.service;

import com.estacionamento.demoparkapi.entity.Usuario;
import com.estacionamento.demoparkapi.exception.EntityNotFoundException;
import com.estacionamento.demoparkapi.exception.PasswordInvalidException;
import com.estacionamento.demoparkapi.exception.UsernameUniqueViolationException;
import com.estacionamento.demoparkapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //Vai ser utilizado para fazer a injeção de dependências direto no construtor, ao invés de utilizar o @Inject
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional //vai indicar que o spring que vai tomar conta da transação
    public Usuario salvar(Usuario usuario){
        //foi adicionado para fazer a captura do lançamento da excessão que é lançada pelo banco de dados, nesse caso é uma específica
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            //Foi criado uma classe para essa exceção, para lançar o erro
            throw new UsernameUniqueViolationException(String.format("Username {%s} já cadastrado", usuario.getUsername()));
        }
    }


    @Transactional(readOnly = true) //essa propriedade readOnly=true informa para o spring que este método é exclusivo para consulta
    //fazendo isto ganhamos em perfomace, pois é uma operação de leitura
    public Usuario buscarPorID(Long id) {
        //usuarioRepository.findById(id); Ele retorna um objeto optional, para resolver este problema podemos fazer de 3 formas
        // 1° forma -> usuarioRepository.findById(id).get()
        // 2° forma -> usuarioRepository.findById(id).orElseGet(Passando uma expressão lambda aqui)
        // 3° forma -> usuarioRepository.findById(id).orElseThrow(); Pondendo lançar um erro caso não encontre
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário não encontrado!!", id))
        );
    }

    //Toda vida eu aprendi por save, vale ressaltar que o hibernat salva na memória cache dele e ao terminar a requisição
    //Ele já sabe que deve salvar as informações no banco de dados por este motivo não foi feito um save no password que funcionaria da
    //mesma forma salvando a senha no banco de dados
    //Como é uma operação de escrita basta só passar o transactional.
    //Importantíssimo ele só faz esta troca por meio da anotação @Transactional, caso não utilize a Transactional, você deve utilizar
    //O método save, para fazer está atualização
    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        //Nesse momento ele está gerenciado pelo Hibernate, como um objeto persistido
        //Se eu tivesse dado um new como objeto Transiente que estaria sendo criado agora, ele precisaria do save

        //verificando se a senha do confirmaSenha é diferente a novaSenha
        if(!confirmaSenha.equals(novaSenha)){
            throw new PasswordInvalidException("Nova senha não confere com a confirmação de senha. ");
        }
        //Passou na primeira verificação faço a busca pelo id
        Usuario user = buscarPorID(id);
        //agora vou conferir se são iguais a senha atual que está no banco e a senha atual informada
        //if(!user.getPassword().equals(senhaAtual)){
        if(!passwordEncoder.matches(senhaAtual, user.getPassword())){
            throw new PasswordInvalidException(("A senha não confere!"));
        }
        //passou pelas verificações ai sim salva a nova senha
        user.setPassword(passwordEncoder.encode(novaSenha));

        return user;
    }

    //Como é uma operação de leitura é basta ativar o readOnly como true
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        List<Usuario> users = usuarioRepository.findAll();
        return users;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário com 'username: %s' não encontrado", username))
        );
    }

    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findByRoleUsername(username);
    }
}
