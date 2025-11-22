package br.com.api.desafio.Auth;

import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.UserAuthResponse;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        boolean matches = passwordEncoder.matches(request.password(), user.getPassword());

        if (!matches) {
            throw new IllegalArgumentException("Senha inválida");
        }

        return new UserAuthResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getDepartment().name()
        );
    }
}

