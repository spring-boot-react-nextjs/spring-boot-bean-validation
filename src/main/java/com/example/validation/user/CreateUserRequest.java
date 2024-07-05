package com.example.validation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotNull(message = "{validation.username.NotNull}")
        @Size(min = 2, max = 50, message = "{validation.username.Size}")
        String username,

        @NotNull(message = "{validation.email.NotNull}")
        @Email(message = "{validation.email.Email}")
        String email
) {
}