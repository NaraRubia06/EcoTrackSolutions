package br.unipar.programacaoweb.ecotracksolutions.model;

import br.unipar.programacaoweb.ecotracksolutions.enums.PermissaoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissaoEnum permissao;

    public Role() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(permissao, role.permissao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissao);
    }
}

// Modelo para perfis/roles, usando enum para permissões.