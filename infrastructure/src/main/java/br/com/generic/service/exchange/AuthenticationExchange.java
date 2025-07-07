package br.com.generic.service.exchange;

import br.com.generic.service.dto.UserDto;
import br.com.generic.service.entity.UserEntity;

import java.util.Optional;

public interface AuthenticationExchange {

    Optional<UserEntity> login(UserDto userDto);

    Optional<UserEntity> refreshToken(Object[] obj);




}
