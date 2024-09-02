package az.edu.turing.unitech.model.mapper;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.model.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto accountEntityToDto(AccountEntity accountEntity);

    AccountEntity accountDtoToAccountEntity(AccountDto accountDto);

    List<AccountDto> accountEntityListToAccountDtoList(List<AccountEntity> accountEntities);

    List<AccountEntity> accountDtoListToAccountEntityList(List<AccountDto> accountDtos);

    void updateAccountEntityFromDto(AccountDto accountDto, @MappingTarget AccountEntity accountEntity);
}
