//package az.edu.turing.service;
//
//import az.edu.turing.dao.entity.UserEntity;
//import az.edu.turing.dao.repository.UserRepository;
//import az.edu.turing.exception.UserNotFoundException;
//import az.edu.turing.mapper.UserMapper;
//import az.edu.turing.model.dto.UserDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    public void login(String finCode, String password) {
//        UserEntity user = userRepository.findByFinCode(finCode)
//                .orElseThrow(() -> new UserNotFoundException(finCode));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Şifre yanlıştır");
//        }
//
//    }
//
//    public UserDto register(UserDto userDto) {
//        if (userRepository.existsByFinCode(userDto.getFinCode())) {
//            throw new RuntimeException("Bu finCode ile zaten bir kullanıcı mevcut");
//        }
//
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        UserEntity userEntity = userMapper.dtoToEntity(userDto);
//        userRepository.save(userEntity);
//
//        return userDto;
//    }
//}
