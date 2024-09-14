package az.edu.turing.config;

import az.edu.turing.mapper.AccountMapper;
import az.edu.turing.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
        @Bean
        public UserMapper userMapper() {
            return Mappers.getMapper(UserMapper.class);
        }
        @Bean
        public AccountMapper accountMapper(){return Mappers.getMapper(AccountMapper.class);}
}
