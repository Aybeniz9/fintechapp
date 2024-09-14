package az.edu.turing.mapper;

import az.edu.turing.model.dto.user.UserDto;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.model.dto.user.UserRegisterRequest;
import az.edu.turing.model.dto.user.UserRegisterResponse;
import az.edu.turing.model.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(UserEntity userEntity);

    UserResponse entityToResponse(UserEntity userEntity);

    List<UserDto> entityListToDtoList(List<UserEntity> userEntity);

    UserEntity dtoToEntity(UserDto userDto);

    UserEntity dtoFromRegisterToEntity(UserRegisterRequest userDto);

    UserRegisterResponse entityToDtoFromResponse(UserEntity userDto);

    void updateEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);
}

