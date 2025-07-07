package br.com.generic.service;

import br.com.generic.service.controller.AuthenticationController;
import br.com.generic.service.dto.TokenDto;
import br.com.generic.service.dto.UserDto;
import br.com.generic.service.entity.UserEntity;
import br.com.generic.service.service.AuthenticationService;
import br.com.generic.service.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisplayName("Testes de Integração - Endpoints de Account Payment")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = AuthenticationController.class)
@Import(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("userPrincipal");
    }

    @Test
    @DisplayName("Deve retornar token JWT para login bem-sucedido")
    public void login_whenValidCredentials_thenReturns200() throws Exception {
        UserDto loginDto = new UserDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("password");

        UserEntity userEntity = new UserEntity();
        String jwtToken = "jwtToken";
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(jwtToken);
        tokenDto.setExpiresIn("1h");

        when(authenticationService.login(eq(loginDto))).thenReturn(Optional.of(userEntity));
        when(jwtService.generateToken(eq(userEntity))).thenReturn(jwtToken);
        when(jwtService.getExpirationTime()).thenReturn(3600000L); // 1 hour in milliseconds

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value(jwtToken))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.expiresIn").value("1 hours"));
    }

    @Test
    @DisplayName("Deve retornar status 401 para credenciais inválidas")
    public void login_whenInvalidCredentials_thenReturns401() throws Exception {
        UserDto loginDto = new UserDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("password");

        when(authenticationService.login(eq(loginDto))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized());
    }
}
