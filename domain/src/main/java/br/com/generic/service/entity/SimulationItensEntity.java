package br.com.generic.service.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_simulation_itens")
public class SimulationItensEntity extends CreateUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_simulation_itens")
    private Long id;

    @Column(name = "term_months")
    private Integer termMonths;

    @Column(name = "monthly_installment")
    private Double monthlyInstallment;

    @Column(name = "interest_rate_percent")
    private Double interestRatePercent;

    @Column(name = "iof")
    private Double iof;

    @Column(name = "tac")
    private Double tac;

    @Column(name = "total_payable")
    private Double totalPayable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_simulation", nullable = false)
    private SimulationEntity simulation;
}
