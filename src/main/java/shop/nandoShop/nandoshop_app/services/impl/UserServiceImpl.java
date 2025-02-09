package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.Role;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password, Role role) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("El email ya está en uso.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, role);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
}
