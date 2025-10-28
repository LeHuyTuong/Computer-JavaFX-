package sum25.se196853.demo.service;

import sum25.se196853.demo.entity.User;

import java.util.List;
import java.util.Optional;
public interface UserService {
    Optional<User> login(String email, String password);
    boolean createUser(User user);
    List<User> getAllUsers();
}
