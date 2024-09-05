package az.edu.turing.unitech.model.mapper;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userEntityToDto(UserEntity userEntity);

    UserEntity userEntityToDto(UserDto userDto);

    List<UserDto> userEntityListToDtoList(List<UserEntity> userEntities);

    List<UserEntity> userDtoListToEntityList(List<UserDto> userDtos);

    void updatedDtoFromEntity(UserDto userDto, @MappingTarget UserEntity userEntity);
}
