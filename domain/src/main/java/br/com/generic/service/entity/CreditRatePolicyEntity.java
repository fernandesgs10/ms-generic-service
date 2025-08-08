package br.com.generic.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_credit_rate_policy")
public class CreditRatePolicyEntity extends CreateUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_income", nullable = false)
    private Double minIncome;

    @Column(name = "max_income", nullable = false)
    private Double maxIncome;

    @Column(name = "min_score", nullable = false)
    private Integer minScore;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @Column(name = "interest_rate_percent", nullable = false)
    private Double interestRatePercent;

    @Column(name = "opening_fee_percent", nullable = false)
    private Double openingFeePercent;

    @Column(name = "iof_percent", nullable = false)
    private Double iofPercent;

    @Column(name = "max_term_in_months", nullable = false)
    private Integer maxTermInMonths;

    @Column(name = "min_amount", nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "grace_period_months")
    private Integer gracePeriodInMonths;

    @Column(name = "monthly_admin_fee")
    private BigDecimal monthlyAdminFee;

    @Column(name = "insurance_percent")
    private Double insurancePercent;

    @Column(name = "early_payment_discount_rate")
    private Double earlyPaymentDiscountRate;

    @Column(name = "policy_code")
    private String policyCode;
}
