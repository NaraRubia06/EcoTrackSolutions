package br.unipar.programacaoweb.ecotracksolutions.repository;

import br.unipar.programacaoweb.ecotracksolutions.enums.PermissaoEnum;
import br.unipar.programacaoweb.ecotracksolutions.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByPermissao(PermissaoEnum permissao);
}
