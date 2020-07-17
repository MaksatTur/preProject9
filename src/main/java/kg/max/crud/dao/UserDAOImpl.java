package kg.max.crud.dao;

import kg.max.crud.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        Query query = entityManager.createQuery("select u from User u", User.class);
        return query.getResultList();
    }

    @Override
    public void delete(long userId) {
       Query query =  entityManager.createQuery("delete from User u where u.id = :id");
       query.setParameter("id", userId).executeUpdate();
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUserById(long id) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class).setParameter("id", id);
        List<User> users = query.getResultList();
        if (!users.isEmpty()){
            return users.get(0);
        }
        return null;
    }

    @Override
    public void insert(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("Select u FROM User u where u.email = :un", User.class).setParameter("un", username);
        return query.getSingleResult();
    }

    @Override
    public String getUserPasswordById(long id) {
        TypedQuery<String> query = entityManager.createQuery("select u.password from User u where u.id = :id", String.class).setParameter("id", id);
        List<String> passwords = query.getResultList();
        if (!passwords.isEmpty()){
            return passwords.get(0);
        }
        return null;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery("select u.id from User u where u.email = :email", Long.class).setParameter("email", username);
        List<Long> ids = query.getResultList();
        if (!ids.isEmpty()){
            return ids.get(0);
        }
        return null;
    }
}
