package az.edu.turing.mapper;

import az.edu.turing.model.dto.UserDto;
import az.edu.turing.dao.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(UserEntity userEntity);

    List<UserDto> entityListToDtoList(List<UserEntity> userEntity);

    UserEntity dtoToEntity(UserDto userDto);

    void updateEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);
}

