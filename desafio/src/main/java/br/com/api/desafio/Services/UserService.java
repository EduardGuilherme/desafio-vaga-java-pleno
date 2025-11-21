package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.CreateUserRequest;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*public UserService(UserRepository repository) {
        this.userRepository = repository;
    }*/

    public User createUser(CreateUserRequest request) {
        validateUserRequest(request);

        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalArgumentException("E-mail já está em uso");
        });

        /*User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordEncoder.encode(request.password())
                .department(Departament.TI) // temporário — vamos ajustar depois
                .build();*/
        User newUser = new User(
                null,
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.departament()
        );

        return userRepository.save(newUser);
    }

    public void validateUserRequest(CreateUserRequest request){
        if(request.name() == null || request.name().isBlank()||
                request.email() == null||request.email().isBlank()||
        request.password() == null||request.password().isBlank()){
            throw new IllegalArgumentException("campos Obrigadotios ausentes");
        }

        if(!request.email().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            throw new IllegalArgumentException("Email Invalido");
        }
    }
}
