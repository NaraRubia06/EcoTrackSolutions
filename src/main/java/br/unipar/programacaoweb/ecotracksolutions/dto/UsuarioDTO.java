package br.unipar.programacaoweb.ecotracksolutions.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    private String username;
    private String nome;
    private String email;
    private String permissao; // ou enum
}
