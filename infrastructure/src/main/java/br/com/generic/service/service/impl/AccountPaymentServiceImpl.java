package br.com.generic.service.service.impl;

import br.com.generic.service.common.GatewayException;
import br.com.generic.service.entity.AccountPaymentEntity;
import br.com.generic.service.router.AccountPayRouter;
import br.com.generic.service.service.AccountPaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class AccountPaymentServiceImpl implements AccountPaymentService {

    private final AccountPayRouter accountPayRouter;

    @Override
    public AccountPaymentEntity save(AccountPaymentEntity accountPaymentEntity) {
        return executeRoute(accountPayRouter.save(), AccountPayRouter.ROUTE_SAVE, accountPaymentEntity);
    }

    @Override
    public AccountPaymentEntity update(AccountPaymentEntity accountPaymentEntity) {
        return executeRoute(accountPayRouter.update(), AccountPayRouter.ROUTE_UPDATE, accountPaymentEntity);
    }

    @Override
    public AccountPaymentEntity findUserById(long id) {
        return executeRoute(accountPayRouter.findUserById(), AccountPayRouter.ROUTE_FIND_USER_BY_ID, id);
    }

    @Override
    public boolean remove(long id) {
        return executeRoute(accountPayRouter.remove(), AccountPayRouter.ROUTE_REMOVE, id);
    }

    @Override
    public Page<AccountPaymentEntity> findAll(Pageable pageable) {
        return executeRoute(accountPayRouter.findAll(), AccountPayRouter.ROUTE_FIND_ALL, pageable);
    }

    public Page<AccountPaymentEntity> findByDueDateAndDescription(LocalDate dueDate, String description, Pageable pageable) {
        return executeRoute(accountPayRouter.findByDueDateAndDescription(), AccountPayRouter.ROUTE_FIND_DUE_DATE_DESCRIPTION, new Object[]{dueDate, description, pageable});
    }

    @Override
    public AccountPaymentEntity refreshToken(String token) {
        return executeRoute(accountPayRouter.refreshToken(), AccountPayRouter.ROUTE_REFRESH_TOKEN, token);
    }

    @Override
    public BigDecimal findSumAmountByPaymentDate(LocalDate paymentDateInit, LocalDate paymentDateLimit) {
        return executeRoute(accountPayRouter.findSumAmountByPaymentDate(), AccountPayRouter.ROUTE_FIND_SUM_AMOUNT, new Object[]{paymentDateInit, paymentDateLimit});
    }

    @Override
    public boolean uploadCsv(MultipartFile csv, String name) {
        return executeRoute(accountPayRouter.uploadCsv(), AccountPayRouter.ROUTE_UPLOAD_CSV, new Object[]{csv, name});
    }


    private <T> T executeRoute(RouteBuilder routeBuilder, String routeId, Object body) {
        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(routeBuilder);
            ctx.start();

            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.requestBody(routeId, body, (Class<T>) Object.class);
            }
        } catch (Exception ex) {
            log.error("Error processing route {}: {}", routeId, ex.getMessage(), ex);
            handleException(ex);
            throw new RuntimeException(ex.getCause().getMessage());
        }
    }

    private void handleException(Exception ex) {
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            if (cause instanceof HttpClientErrorException.BadRequest) {
                throw (HttpClientErrorException.BadRequest) cause;
            } else if (cause instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) cause;
            } else if (cause instanceof GatewayException) {
                throw (GatewayException) cause;
            }
        }
    }
}
