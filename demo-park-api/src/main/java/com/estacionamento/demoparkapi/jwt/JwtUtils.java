package com.estacionamento.demoparkapi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j //serve para log
public class JwtUtils {
    //Vai criar por aqui o token pela utisl
    public static final String JWT_BEARER = "Bearer";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789"; //Aqui vai precisar que tenha no mínimo 32 caracteres para fazer a criptografia dessa chave
    public static final long EXPIRE_DAYS = 0; //Vai armazenar o tempo que o token vai levar para expirar
    public static final long EXPIRE_HOURS = 0;   //Vai armazenar o tempo que o token vai levar para expirar
    public static final long EXPIRE_MINUTES = 10 ;   //Vai armazenar o tempo que o token vai levar para expirar

    private JwtUtils() {} //O construtor vai ser privado pois só vamos trabalhar com métodos staticos aqui

    //preparando e criptografando a chave
    private static Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    //processo de expiração do token
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); //Pegando a zona do sistema e o tempo
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    //Criando o token
    public static JwtToken createToken(String userName, String role) {
        Date issuedAt = new Date(); //Data de criação do token
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ","JWT") //adicionando o parâmetro
                .setSubject(userName) //vai fazer a consulta no banco de dados para verificar se o nome do usuário realmente bate
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256) //chave e algoritmo de criptografia
                .claim("role", role) //é utilizado quando queremos adicionar alguma informação no token e não existe um método específico para isso, podemos utilizar vários dele
                .compact();
        return new JwtToken(token);
    }

    //recupera o conteúdo do token
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)) //Tem que remover do token a instrução Barrear
                    .getBody();
        } catch (JwtException e) {
            log.error(String.format("Token inválido %s", e.getMessage()));
        }

        return null;
    }

    public static String getusernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {
             Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)); //Tem que remover do token a instrução Barrear

             return false;
        } catch (JwtException e) {
            log.error(String.format("Token inválido %s", e.getMessage()));
        }

        return true;
    }

    private static String refactorToken(String token) {
        if(token.contains(JWT_BEARER)) {
          token = token.substring(JWT_BEARER.length());
          token = token.trim();
        }
        return token;
    }
}
