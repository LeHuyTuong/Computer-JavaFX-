package sum25.se196853.demo.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.repository.UserRepository;
import sum25.se196853.demo.service.UserService;
import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.repository.UserRepository;
import sum25.se196853.demo.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> login(String email, String password) {
        // return userRepository.findByEmailAndPassword(email, password);
        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);
        return userOpt;
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        // Kiểm tra email tồn tại trước khi tạo
        if (userRepository.findByEmailAndPassword(user.getEmail(), user.getEmail()).isPresent()) {
            System.err.println("User creation failed: Email already exists - " + user.getEmail());
            return false;
        }
        return userRepository.save(user) != null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
