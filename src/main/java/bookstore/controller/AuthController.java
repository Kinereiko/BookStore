package bookstore.controller;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RuntimeException {
        return userService.register(request);
    }
}
