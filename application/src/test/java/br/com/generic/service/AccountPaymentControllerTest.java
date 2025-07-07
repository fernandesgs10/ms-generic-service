package br.com.generic.service;

import br.com.generic.service.controller.AccountPaymentController;
import br.com.generic.service.dto.AccountPaymentDto;
import br.com.generic.service.entity.AccountPaymentEntity;
import br.com.generic.service.mapper.AccountPaymentMapper;
import br.com.generic.service.service.AccountPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisplayName("Testes de Integração - Endpoints de Account Payment")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = AccountPaymentController.class)
@Import(AccountPaymentController.class)
public class AccountPaymentControllerTest {

    public static final String ENDPOINT_ACCOUNT = "/v1/account-payment";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountPaymentMapper accountPaymentMapper;

    @Mock
    private AccountPaymentMapper accountPaymentMapper2;

    @MockBean
    private AccountPaymentService accountPaymentService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("desafio");
        ModelMapper modelMapper = new ModelMapper();
        when(accountPaymentMapper.getModelMapper()).thenReturn(modelMapper);
    }

    @Test
    @DisplayName("Deve salvar um Account Payment e retornar status 201")
    public void saveAccount_whenValidInput_thenReturns201() throws Exception {
        AccountPaymentEntity accountPaymentEntity = new AccountPaymentEntity();
        AccountPaymentDto accountPaymentDto = createTestAccountPaymentDto();

        when(accountPaymentMapper.converterDtoObjectToEntity(any(AccountPaymentDto.class)))
                .thenReturn(accountPaymentEntity);
        when(accountPaymentMapper.converterEntityObjectToDto(any(AccountPaymentEntity.class)))
                .thenReturn(accountPaymentDto);
        when(accountPaymentService.save(any(AccountPaymentEntity.class)))
                .thenReturn(accountPaymentEntity);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPaymentDto))
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve atualizar um Account Payment e retornar status 200")
    public void updateAccountPayment_withValidInput_thenReturns200() throws Exception {
        AccountPaymentEntity accountPaymentEntity = new AccountPaymentEntity();
        AccountPaymentDto accountPaymentDto = createTestAccountPaymentDto();

        when(accountPaymentMapper.converterDtoObjectToEntity(any(AccountPaymentDto.class)))
                .thenReturn(accountPaymentEntity);
        when(accountPaymentService.update(any(AccountPaymentEntity.class)))
                .thenReturn(accountPaymentEntity);
        when(accountPaymentMapper.converterEntityObjectToDto(any(AccountPaymentEntity.class)))
                .thenReturn(accountPaymentDto);

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPaymentDto))
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar a lista de Account Payments com status 200")
    public void getAccountPayments_thenReturns200() throws Exception {
        AccountPaymentDto accountPaymentDto = createTestAccountPaymentDto();
        List<AccountPaymentDto> accountPaymentDtoList = Collections.singletonList(accountPaymentDto);

        AccountPaymentEntity accountPaymentEntity = new AccountPaymentEntity();
        accountPaymentEntity.setId(1L);
        accountPaymentEntity.setAmount(new BigDecimal("150.75"));
        accountPaymentEntity.setDueDate(LocalDate.now().plusDays(30));
        accountPaymentEntity.setDescription("Payment for monthly subscription");
        accountPaymentEntity.setStatus(true);

        List<AccountPaymentEntity> entityList = new ArrayList<>();
        entityList.add(accountPaymentEntity);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AccountPaymentEntity> page = new PageImpl<>(entityList, pageable, entityList.size());

        when(accountPaymentService.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(AccountPaymentEntity.class), any(Class.class)))
                .thenAnswer(invocation -> {
                    AccountPaymentEntity entity = invocation.getArgument(0);
                    return createTestAccountPaymentDto();
                });

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Deve excluir um Account Payment e retornar status 200")
    public void deleteAccountPayment_whenValidId_thenReturns204() throws Exception {
        Long accountPaymentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_ACCOUNT + "/remove/id/" + accountPaymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve retornar status 404 quando tentar excluir um Account Payment com ID inválido")
    public void deleteAccountPayment_whenInvalidId_thenReturns404() throws Exception {
        Long invalidId = 999L;

        Mockito.doThrow(new RuntimeException("Account Payment not found")).when(accountPaymentService).remove(invalidId);

        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_ACCOUNT + "/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve fazer upload de arquivo e retornar status 200")
    public void uploadFile_whenValidFile_thenReturns200() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", "text/plain", "file content".getBytes());


        when(accountPaymentService.uploadCsv(any(MockMultipartFile.class), anyString()))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart(ENDPOINT_ACCOUNT + "/upload/csv")
                        .file(mockMultipartFile))
                .andExpect(status().isOk());

    }

    private AccountPaymentDto createTestAccountPaymentDto() {
        AccountPaymentDto accountPaymentDto = new AccountPaymentDto();
        accountPaymentDto.setDueDate(LocalDate.now().plusDays(30));
        accountPaymentDto.setPaymentDate(null);
        accountPaymentDto.setAmount(new BigDecimal("150.75"));
        accountPaymentDto.setDescription("Payment for monthly subscription");
        accountPaymentDto.setStatus(true);
        return accountPaymentDto;
    }
}