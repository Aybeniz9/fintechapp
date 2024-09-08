package az.edu.turing.mapper;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.model.dto.account.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountResponseMapper {
    AccountResponseMapper INSTANCE = Mappers.getMapper(AccountResponseMapper.class);

    AccountResponse entityToDto(AccountEntity accountEntity);

    AccountEntity dtoToEntity(AccountResponse accountResponse);
}
