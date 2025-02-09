package shop.nandoShop.nandoshop_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.Role;
import shop.nandoShop.nandoshop_app.repositories.UserRepository;

import javax.xml.validation.Validator;
import java.util.Optional;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password, String firstName, String lastName, Role role) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("El email ya está en uso.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword,firstName,lastName, role);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public void grantSellerRole(Long userId) {
        // Buscar el usuario por ID
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = optionalUser.get();

        // Verificar que el usuario no sea ya un SELLER
        if (user.getRole() == Role.SELLER) {
            throw new RuntimeException("El usuario ya tiene el rol de vendedor");
        }

        // Asignar el nuevo rol y guardar
        user.setRole(Role.SELLER);
        userRepository.save(user);
    }
}
