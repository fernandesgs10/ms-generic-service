package br.com.generic.service.service;

import br.com.generic.service.dto.UserDto;
import br.com.generic.service.entity.UserEntity;

import java.util.Optional;

public interface AuthenticationService {

    Optional<UserEntity> login(UserDto userDto);


    Optional<UserEntity> refreshToken(String token, String tokenBearer);




}
