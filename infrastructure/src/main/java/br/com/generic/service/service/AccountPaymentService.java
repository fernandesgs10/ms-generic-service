package br.com.generic.service.service;

import br.com.generic.service.entity.AccountPaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccountPaymentService {


    AccountPaymentEntity save(AccountPaymentEntity accountPaymentEntity);

    AccountPaymentEntity update(AccountPaymentEntity accountPaymentEntity);

    boolean remove(long id);

    AccountPaymentEntity findUserById(long id);

    Page<AccountPaymentEntity> findAll(Pageable pageable);

    Page<AccountPaymentEntity> findByDueDateAndDescription(LocalDate dueDate, String description, Pageable pageable);

    AccountPaymentEntity refreshToken(String token);

    BigDecimal findSumAmountByPaymentDate(LocalDate paymentDateInit, LocalDate paymentDateLimit);

    boolean uploadCsv(MultipartFile csv, String name);


}
