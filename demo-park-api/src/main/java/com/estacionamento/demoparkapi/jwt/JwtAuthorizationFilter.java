package com.estacionamento.demoparkapi.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailService detailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if(token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("JWT Token está nullo, vazio ou não iniciado com Bearer");
            filterChain.doFilter(request, response);
            return;
        }
        if(JwtUtils.isTokenValid(token)) {
            log.info("O JWT Token está inválido ou expirado.");
            filterChain.doFilter(request, response);
            return;
        }

        String username = JwtUtils.getusernameFromToken(token);

        toAthenticationn(request, username);

        filterChain.doFilter(request, response);

    }

    private void toAthenticationn(HttpServletRequest request, String username){
        UserDetails userDetails = detailService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());
        //Aqui estamos passando o objeto de requisição para a parte de autenticaão do spring security
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //Uma classe do spring security que da acesso ao contexto de segurança do spring
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
