package br.com.api.desafio.Services;

import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        return repository.save(user);
    }
}
