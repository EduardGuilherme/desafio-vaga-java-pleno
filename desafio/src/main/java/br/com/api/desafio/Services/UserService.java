package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.CreateUserRequest;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /*public UserService(UserRepository repository) {
        this.userRepository = repository;
    }*/

    public User createUser(CreateUserRequest request) {

        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalArgumentException("E-mail já está em uso");
        });

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password()) // será criptografada depois
                .department(Departament.TI) // temporário — vamos ajustar depois
                .build();

        return userRepository.save(newUser);
    }
}
