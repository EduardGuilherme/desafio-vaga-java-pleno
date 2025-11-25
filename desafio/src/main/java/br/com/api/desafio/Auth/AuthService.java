package br.com.api.desafio.Auth;

import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.TokenResponseDTO;
import br.com.api.desafio.Dtos.UserAuthResponse;
import br.com.api.desafio.Exceptions.InvalidPasswordException;
import br.com.api.desafio.Exceptions.UserNotFoundException;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import br.com.api.desafio.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponseDTO login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        //boolean matches = passwordEncoder.matches(request.password(), user.getPassword());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Senha inválida");
        }

        String token = jwtUtil.generateToken(user.getEmail(),
                user.getDepartment() != null ? user.getDepartment().name() : "USER",
                user.getId());

        return new TokenResponseDTO(token, "Bearer", user.getId(), user.getEmail(), user.getName());
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}

