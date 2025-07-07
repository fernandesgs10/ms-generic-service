package br.com.generic.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AccountPaymentDto {

    private Long id;

    @NotNull(message = "{accountpayment.dueDate.notempty}")
    private LocalDate dueDate;

    private LocalDate paymentDate;

    @NotNull(message = "{accountpayment.amount.notempty}")
    @PositiveOrZero(message = "{accountpayment.amount.positive}")
    private BigDecimal amount;

    @Size(max = 255, message = "{accountpayment.description.length}")
    private String description;

    @NotNull(message = "{accountpayment.status.notnull}")
    private Boolean status;

    private Long version;

}