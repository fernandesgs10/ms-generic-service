package br.com.generic.service.repository;

import br.com.generic.service.entity.AccountPaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface AccountPaymentRepository extends JpaRepository<AccountPaymentEntity, Long> {

    Page<AccountPaymentEntity> findAll(Pageable pageable);

    @Query("SELECT SUM(ap.amount) FROM AccountPaymentEntity ap WHERE ap.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal findSumAmountByPaymentDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
