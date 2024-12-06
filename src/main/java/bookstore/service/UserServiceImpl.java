package bookstore.service;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import bookstore.exception.RegistrationException;
import bookstore.mapper.UserMapper;
import bookstore.model.User;
import bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsById(Long.valueOf(requestDto.getEmail()))) {
            throw new RegistrationException("Email already exists: " + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
