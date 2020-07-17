package kg.max.crud.dao;

import kg.max.crud.model.User;

import java.util.List;

public interface UserDAO {
    List<User> findAll();

    void delete(long userId);

    void update(User user);

    User getUserById(long id);

    void insert(User user);

    User findByUsername(String username);

    String getUserPasswordById(long id);

    Long getUserIdByUsername(String username);
}
