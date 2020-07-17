package kg.max.crud.service;

import kg.max.crud.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    List<User> findAll();

    void delete(long userId);

    void update(User user);

    User getUserById(long id);

    void insert(User user);

    String getUserPasswordById(long id);

    Long getUserIdByUsername(String username);
}
