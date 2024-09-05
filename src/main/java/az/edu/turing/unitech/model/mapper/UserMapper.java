package az.edu.turing.unitech.model.mapper;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity userDtoToEntity(UserDto userDto);

    UserDto userEntityToDto(UserEntity userEntity);

    List<UserDto> userEntityListToDtoList(List<UserEntity> userEntities);

    List<UserEntity> userDtoListToEntityList(List<UserDto> userDtos);

    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);
}


