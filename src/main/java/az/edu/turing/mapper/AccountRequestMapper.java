package az.edu.turing.mapper;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.model.dto.account.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountRequestMapper {

    AccountRequestMapper INSTANCE = Mappers.getMapper(AccountRequestMapper.class);

    AccountRequest entityToDto(AccountEntity accountEntity);

//    List<UserDto> entityListToDtoList(List<UserEntity> userEntity);

    AccountEntity dtoToEntity(AccountRequest accountRequest);
}
