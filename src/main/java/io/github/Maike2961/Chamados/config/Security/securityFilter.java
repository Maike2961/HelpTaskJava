package io.github.Maike2961.Chamados.config.Security;

import io.github.Maike2961.Chamados.config.CustomUserDetailService;
import io.github.Maike2961.Chamados.service.token.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class securityFilter extends OncePerRequestFilter {

    @Autowired
    private Token tokenValida;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    public String headerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.split(" ")[0].equals("Bearer")) {
            return null;
        }
        return authorization.split(" ")[1];
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = headerToken(request);

        System.out.println("Token recebido: " + token);
        if (token != null) {
            String username = tokenValida.validateToken(token);
            System.out.println("Usuário extraido do token: " + username);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            System.out.println("UserDetail: " + userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            System.out.println("Token: " + authToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

}
