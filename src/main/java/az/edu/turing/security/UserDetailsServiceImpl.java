//package az.edu.turing.security;
//
//import az.edu.turing.dao.entity.UserEntity;
//import az.edu.turing.dao.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList; // Eğer yetkiler gerekliyse, burayı güncellemelisiniz.
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String findCode) {
//        UserEntity userEntity = userRepository.findByFinCode(findCode)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found by finCode"));
//
//        // UserEntity'nin email ve şifre alanlarının doğru olduğundan emin olun.
//        return new org.springframework.security.core.userdetails.User(
//                userEntity.getEmail(),
//                userEntity.getPassword(),
//                new ArrayList<>() // Yetkiler varsa burada ekleyin.
//        );
//    }
//}
