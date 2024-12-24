package bookstore.service;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import javax.management.relation.RoleNotFoundException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RoleNotFoundException;
}
