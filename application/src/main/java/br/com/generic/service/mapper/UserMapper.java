package br.com.generic.service.mapper;

import br.com.generic.service.dto.UserDto;
import br.com.generic.service.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@Component
@AllArgsConstructor
public class UserMapper implements Serializable {

    private final ModelMapper modelMapper;

    public UserEntity converterDtoObjectToEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public UserEntity converterDtoObjectToMongo(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public UserDto converterEntityObjectToDto(UserEntity userMongo) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userMongo, UserDto.class);
    }
}