package br.com.generic.service.infrastructure;

import br.com.generic.service.Utils.UserUtil;
import br.com.generic.service.common.GatewayException;
import br.com.generic.service.config.MensagemComponent;
import br.com.generic.service.entity.AccountPaymentEntity;
import br.com.generic.service.exchange.AccountPayExchange;
import br.com.generic.service.repository.AccountPaymentRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.assertj.core.util.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AccountPaymentExchangeImpl implements AccountPayExchange {

    private final MensagemComponent mensagemComponent;
    private final AccountPaymentRepository accountPaymentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AccountPaymentEntity save(AccountPaymentEntity accountPaymentEntity) {
        validateAccountPaymentEntity(accountPaymentEntity);
        return accountPaymentRepository.save(accountPaymentEntity);
    }

    @Override
    public AccountPaymentEntity update(AccountPaymentEntity accountPaymentEntity) {
        AccountPaymentEntity existingEntity = findUserById(accountPaymentEntity.getId());

        Optional.ofNullable(accountPaymentEntity.getNmEdited()).ifPresent(existingEntity::setNmEdited);
        Optional.ofNullable(accountPaymentEntity.getDueDate()).ifPresent(existingEntity::setDueDate);
        Optional.ofNullable(accountPaymentEntity.getPaymentDate()).ifPresent(existingEntity::setPaymentDate);
        Optional.ofNullable(accountPaymentEntity.getAmount()).ifPresent(existingEntity::setAmount);
        Optional.ofNullable(accountPaymentEntity.getDescription()).ifPresent(existingEntity::setDescription);
        Optional.ofNullable(accountPaymentEntity.getStatus()).ifPresent(existingEntity::setStatus);

        return accountPaymentRepository.save(existingEntity);
    }

    @Override
    public boolean remove(Long id) {
        AccountPaymentEntity existingEntity = findUserById(id);
        if (existingEntity != null) {
            accountPaymentRepository.delete(existingEntity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AccountPaymentEntity findUserById(Long id) {
        Preconditions.checkArgument(id != null,
                mensagemComponent.getMessage(
                        "accountpayment.id.notnull"
                ));

        Optional<AccountPaymentEntity> optionalEntity = accountPaymentRepository.findById(id);
        return optionalEntity.orElseThrow(() -> new GatewayException(mensagemComponent.getMessage(
                "accountpayment.id.notfound", id
        )));
    }

    @Override
    public Page<AccountPaymentEntity> findAll(Pageable pageable) {
        return accountPaymentRepository.findAll(pageable);
    }


    @Override
    public Page<AccountPaymentEntity> findByDueDateAndDescription(Object[] objects) {
        Date startDate = UserUtil.parseDate(objects[0]);
        String descriptionPattern = (String) objects[1];
        Pageable pageable = (Pageable) objects[2];

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<AccountPaymentEntity> criteriaQuery = criteriaBuilder.createQuery(AccountPaymentEntity.class);
        Root<AccountPaymentEntity> root = criteriaQuery.from(AccountPaymentEntity.class);

        Predicate dueDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), startDate);
        Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), "%" + descriptionPattern + "%");
        Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), false);

        criteriaQuery.where(criteriaBuilder.and(dueDatePredicate, descriptionPredicate, statusPredicate));

        TypedQuery<AccountPaymentEntity> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<AccountPaymentEntity> entities = query.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<AccountPaymentEntity> countRoot = countQuery.from(AccountPaymentEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(countRoot.get("dueDate"), startDate),
                criteriaBuilder.like(countRoot.get("description"), "%" + descriptionPattern + "%"),
                criteriaBuilder.equal(countRoot.get("status"), false)
        ));

        TypedQuery<Long> countTypedQuery = entityManager.createQuery(countQuery);
        Long totalElements = countTypedQuery.getSingleResult();

        return new PageImpl<>(entities, pageable, totalElements);
    }

    @Override
    public BigDecimal findSumAmountByPaymentDate(Object obj[]) {
        LocalDate paymentDateInit = (LocalDate) obj[0];
        LocalDate paymentDateLimit = (LocalDate) obj[1];

        return accountPaymentRepository.findSumAmountByPaymentDateBetween(paymentDateInit, paymentDateLimit);
    }

    @SneakyThrows
    @Override
    public boolean uploadCsv(Object obj[]) {
        MultipartFile csv = (MultipartFile) obj[0];
        String name = (String) obj[1];

        List<AccountPaymentEntity> accountPayments = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(csv.getInputStream()))) {
            List<String[]> rows = csvReader.readAll();

            rows.stream()
                    .filter(row -> row.length >= 5)
                    .forEach(row -> {
                        if (Objects.isNull(row[0]) || Objects.isNull(row[1]) || Objects.isNull(row[2]) || Objects.isNull(row[3]) || Objects.isNull(row[4])) {
                            return;
                        }

                        LocalDate dueDate;
                        LocalDate paymentDate = null;
                        BigDecimal amount;
                        String description;
                        boolean status;

                        try {
                            dueDate = LocalDate.parse(row[0]);
                            if (Boolean.parseBoolean(row[4])) {
                                paymentDate = LocalDate.parse(row[1]);
                            }
                            amount = new BigDecimal(row[2]);
                            description = row[3];
                            status = Boolean.parseBoolean(row[4]);
                        } catch (Exception e) {
                            return;
                        }

                        AccountPaymentEntity accountPaymentEntity = new AccountPaymentEntity();
                        accountPaymentEntity.setDueDate(dueDate);
                        accountPaymentEntity.setPaymentDate(paymentDate);
                        accountPaymentEntity.setAmount(amount);
                        accountPaymentEntity.setDescription(description);
                        accountPaymentEntity.setStatus(status);
                        accountPaymentEntity.setNmCreated(name);

                        validateAccountPaymentEntity(accountPaymentEntity);

                        accountPayments.add(accountPaymentEntity);
                    });

            accountPaymentRepository.saveAll(accountPayments);

            return true;

        } catch (IOException | CsvException e) {
               throw e;
        }
    }


    private void validateAccountPaymentEntity(AccountPaymentEntity accountPaymentEntity) {
        String dueDateErrorMessage = mensagemComponent.getMessage("account.payment.dueDate.required");
        String statusErrorMessage = mensagemComponent.getMessage("account.payment.status.required");
        String paymentDateErrorMessage = mensagemComponent.getMessage("account.payment.paymentDate.future");

        Preconditions.checkArgument(
                !(accountPaymentEntity.getStatus() && accountPaymentEntity.getPaymentDate() == null),
                dueDateErrorMessage
        );

        Preconditions.checkArgument(
                !(!accountPaymentEntity.getStatus() && accountPaymentEntity.getPaymentDate() != null),
                statusErrorMessage
        );

        Preconditions.checkArgument(
                accountPaymentEntity.getPaymentDate() == null || !accountPaymentEntity.getPaymentDate().isAfter(LocalDate.now()),
                paymentDateErrorMessage
        );

        Preconditions.checkArgument(
                !accountPaymentEntity.getStatus() || accountPaymentEntity.getDueDate() != null,
                dueDateErrorMessage
        );
    }

}
