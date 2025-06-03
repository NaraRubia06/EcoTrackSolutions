package br.unipar.programacaoweb.ecotracksolutions.repository;

import br.unipar.programacaoweb.ecotracksolutions.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);
    Optional<Usuario> findByUsername(String username);
}
