package br.unipar.programacaoweb.ecotracksolutions.configuration;

import br.unipar.programacaoweb.ecotracksolutions.dto.LoginDTO;
import br.unipar.programacaoweb.ecotracksolutions.dto.LoginResponse;
import br.unipar.programacaoweb.ecotracksolutions.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public AuthorizationService(UsuarioRepository usuarioRepository,
                                TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    public LoginResponse doLogin(LoginDTO loginDTO) {
        var user = loadUserByUsername(loginDTO.username());
        return new LoginResponse(
                user.getUsername(),
                tokenService.generateToken(user)
        );
    }
}
