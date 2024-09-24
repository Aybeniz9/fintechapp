package az.edu.turing.security;

import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String finCode) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + finCode));
        return new org.springframework.security.core.userdetails.User(
                user.getFinCode(), user.getPassword(), new ArrayList<>()
        );
    }
}