package br.unipar.programacaoweb.ecotracksolutions.configuration;

import br.unipar.programacaoweb.ecotracksolutions.model.Usuario;
import br.unipar.programacaoweb.ecotracksolutions.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService,
                          UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    private String getToken(HttpServletRequest request)
            throws Exception {
        var token = request.getHeader("Authorization");
        if(token == null || token.isEmpty()) {
            throw new Exception("Token não encontrado!");
        }
        else if(!token.startsWith("Bearer ")) {
            throw new Exception("Token inválido!");
        }

        return token.replace("Bearer ", "");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            String subject = tokenService.getSubjectByToken(token); // CHAMADA CORRIGIDA

            Usuario user = usuarioRepository.findByUsername(subject)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + subject));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}
