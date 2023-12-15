package com.estacionamento.demoparkapi.web.exception;


import com.estacionamento.demoparkapi.exception.CpfUniqueVioletionExceiption;
import com.estacionamento.demoparkapi.exception.EntityNotFoundException;
import com.estacionamento.demoparkapi.exception.PasswordInvalidException;
import com.estacionamento.demoparkapi.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j //Anotação do lombok para usar o log.error, para lançar a exceção no console
//Ela funciona como uma espécie de ouvinte que captura as excessões lançadas e fazer o tratamento que vamos indicar nos
//métodos aqui na classe
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api-error:", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request,HttpStatus.FORBIDDEN,ex.getMessage()));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api-error:", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request,HttpStatus.BAD_REQUEST,ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    //Tem um pulo do gato aqui, eu posso passar no argumento UsernameUniqueViolationException, mas posso também passar RuntimeException
    //Que ele passa a capturar todas runtimeException do mesmo tipo e já tratalas
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
                                                                         HttpServletRequest request) {
        log.error("Api Error - ", ex);
        //Devolvendo a resposta, to retornando o 409, que é um erro quando a aplicação não consegue processar o que o cliente enviou
        //Por exemplo, campos enviados de forma errada, faltando ou mal formatado
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }


//A anotação @ExceptionHandler aceita um array, facilitando para que seja adicionada mais de uma exceção do mesmo tipo para ser chamado quando occorrer um erro do tipo
    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueVioletionExceiption.class})
    //Tem um pulo do gato aqui, eu posso passar no argumento UsernameUniqueViolationException, mas posso também passar RuntimeException
    //Que ele passa a capturar todas runtimeException do mesmo tipo e já tratalas
    //Como essa classe eu criei, o BidingResult não existe, logo ele vai dá erro 500, para resolver é só não passar o argumento do result
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(RuntimeException ex,
                                                                         HttpServletRequest request) {
        log.error("Api Error - ", ex);
        //Devolvendo a resposta, to retornando o 409, que é um erro quando a aplicação não consegue processar o que o cliente enviou
        //Por exemplo, campos enviados de forma errada, faltando ou mal formatado
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class) //É aqui que vamos registrar a exceção que queremos que a classe capture
    //Sempre que ela for lançada vai cair aqui nesse método
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request, BindingResult result) {
        log.error("Api Error - ", ex);
        //Devolvendo a resposta, to retornando o 422, que é um erro quando a aplicação não consegue processar o que o cliente enviou
        //Por exemplo, campos enviados de forma errada, faltando ou mal formatado
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campos inválidos", result));
    }



}
