package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.CreateUserRequest;
import br.com.api.desafio.Dtos.UpdateUserRequest;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*public UserService(UserRepository repository) {
        this.userRepository = repository;
    }*/

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

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

    public User updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (request.name() != null && !request.name().isBlank())
            user.setName(request.name());

        if (request.email() != null && !request.email().isBlank())
            user.setEmail(request.email());

        if (request.password() != null && !request.password().isBlank())
            user.setPassword(passwordEncoder.encode(request.password()));

        if (request.departament() != null)
            user.setDepartment(request.departament());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
}
