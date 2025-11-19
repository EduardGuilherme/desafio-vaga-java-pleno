package br.com.api.desafio.users;

import br.com.api.desafio.model.User;
import br.com.api.desafio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        User user = new User();
        user.setName("Eduardo");
        user.setEmail("eduardo@email.com");
        user.setPassword("123456");

        when(repository.save(user)).thenReturn(user);

        // Act
        User result = service.createUser(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Eduardo");
        assertThat(result.getEmail()).isEqualTo("eduardo@email.com");
    }
}
