package bookstore.service;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.exception.RegistrationException;
import bookstore.mapper.UserMapper;
import bookstore.model.Role;
import bookstore.model.User;
import bookstore.repository.RoleRepository;
import bookstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Email already exists: " + requestDto.getEmail());
        }
        Optional<Role> defaultRole = roleRepository.findByRole(Role.RoleName.USER);
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.getRoles().add(defaultRole
                .orElseThrow(() -> new EntityNotFoundException("Can't find role: "
                        + Role.RoleName.USER)));
        return userMapper.toDto(userRepository.save(user));
    }
}
