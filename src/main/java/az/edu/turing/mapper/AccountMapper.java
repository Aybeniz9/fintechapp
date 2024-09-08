package az.edu.turing.mapper;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.model.dto.account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto entityToDto(AccountEntity accountEntity);

    List<AccountDto> entityListToDtoList(List<AccountEntity> accountEntities);

    AccountEntity dtoToEntity(AccountDto accountDto);

    void updateEntityFromDto(AccountDto accountDto, @MappingTarget AccountEntity accountEntity);
}
