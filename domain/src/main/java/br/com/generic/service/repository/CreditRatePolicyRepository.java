package br.com.generic.service.repository;

import br.com.generic.service.entity.CreditRatePolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRatePolicyRepository extends JpaRepository<CreditRatePolicyEntity, Long> {

    @Query("SELECT c FROM CreditRatePolicyEntity c " +
            "WHERE :income BETWEEN c.minIncome AND c.maxIncome " +
            "AND :score BETWEEN c.minScore AND c.maxScore")
    List<CreditRatePolicyEntity> findMatchingPolicies(@Param("income") Double income,
                                                      @Param("score") Integer score);

    @Query("SELECT r FROM CreditRatePolicyEntity r WHERE r.minIncome = 0 AND r.maxIncome = 9999999 AND r.minScore = 0 AND r.maxScore = 9999")
    Optional<CreditRatePolicyEntity> findDefaultPolicy();



}

