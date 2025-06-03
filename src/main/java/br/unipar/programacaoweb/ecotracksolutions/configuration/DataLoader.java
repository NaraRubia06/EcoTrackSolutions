package br.unipar.programacaoweb.ecotracksolutions.configuration;

import br.unipar.programacaoweb.ecotracksolutions.enums.PermissaoEnum;
import br.unipar.programacaoweb.ecotracksolutions.model.Role;
import br.unipar.programacaoweb.ecotracksolutions.model.Usuario;
import br.unipar.programacaoweb.ecotracksolutions.repository.RoleRepository;
import br.unipar.programacaoweb.ecotracksolutions.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setPermissao(PermissaoEnum.ADMIN);
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setPermissao(PermissaoEnum.USER);
            roleRepository.save(userRole);

            System.out.println("Roles ADMIN e USER criadas.");
        }

        if (usuarioRepository.count() == 0) {
            // Busca o role ADMIN criado ou existente
            Role adminRole = roleRepository.findByPermissao(PermissaoEnum.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada"));

            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getListRoles().add(adminRole); // associa a Role ADMIN ao usuário
            usuarioRepository.save(admin);

            System.out.println("Usuário admin criado: admin/admin123");
        }
    }
}
