package com.example.validation.user;

import com.example.validation.exception.ResourceNotFoundException;
import com.example.validation.i18n.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public record UserService(I18nService i18nService) {

    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder()
                        .email("john@test.com")
                        .username("john-doe")
                .build()
        );
        users.add(User.builder()
                .email("jane@test.com")
                .username("jane-doe")
                .build()
        );

        return users;
    }

    public User getUserByUsername(String username) {
        Optional<User> user = this.findUsernameInUserList(username);
        if (user.isEmpty()) {
            log.error(i18nService.getLogMessage("user.not.found.log"), username);
            throw new ResourceNotFoundException("user.not.found", username);
        }
        return user.get();
    }

    private Optional<User> findUsernameInUserList(String username) {
        List<User> allUsers = this.getAllUsers();
        return allUsers.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findAny();
    }

    public User createUser(CreateUserRequest request) {
        List<User> allUsers = this.getAllUsers();
        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .build();
        allUsers.add(newUser);
        return newUser;
    }
}