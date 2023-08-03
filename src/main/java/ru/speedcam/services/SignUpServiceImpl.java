package ru.speedcam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.speedcam.forms.UserForm;
import ru.speedcam.models.User;
import ru.speedcam.models.UserRole;
import ru.speedcam.models.UserState;
import ru.speedcam.repositories.UserRepository;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public void signUp(UserForm userForm) {
        String hashPassword = passwordEncoder.encode(userForm.getPassword());

        User user = User.builder()
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .hashPassword(hashPassword)
                .login(userForm.getLogin())
                .role(UserRole.USER)
                .state(UserState.ACTIVE)
                .build();

        user.setId(1L);

        userRepository.save(user);
    }
}