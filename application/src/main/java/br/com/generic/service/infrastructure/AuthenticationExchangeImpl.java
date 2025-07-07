package br.com.generic.service.infrastructure;

import br.com.generic.service.Utils.EncryptionPwUtil;
import br.com.generic.service.dto.UserDto;
import br.com.generic.service.entity.UserEntity;
import br.com.generic.service.exchange.AuthenticationExchange;
import br.com.generic.service.repository.AuthenticationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("rawtypes")
@Service
@AllArgsConstructor
public class AuthenticationExchangeImpl implements AuthenticationExchange {

    private final AuthenticationRepository authenticationRepository;

    @Override
    public Optional<UserEntity> login(UserDto userDto) {
        String encryptPassword = EncryptionPwUtil.encrypt(userDto.getPassword());
        Optional<UserEntity> userMongo = authenticationRepository.findByEmailAndPassword(userDto.getEmail(), encryptPassword);
        return userMongo;
    }

    //TODO....
    @Override
    public Optional<UserEntity> refreshToken(Object[] obj) {
        return null;
    }

}
