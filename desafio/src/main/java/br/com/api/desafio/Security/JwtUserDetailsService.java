package br.com.api.desafio.Security;

import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userReposiroty;

    public JwtUserDetailsService(UserRepository userRepository){
        this.userReposiroty = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userReposiroty.findByEmail(username);
        User user = opt.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Role simples usando departamento (pode adaptar)
        String role = user.getDepartment() != null ? "ROLE_" + user.getDepartment().name() : "ROLE_USER";
        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
