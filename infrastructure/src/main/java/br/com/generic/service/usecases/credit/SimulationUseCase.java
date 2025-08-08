package br.com.generic.service.usecases.credit;

import br.com.generic.service.account.SimulationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SimulationUseCase {

    Page<SimulationResponse> execute(String cpf, Double income, Double requestedAmount, Integer score, Pageable pageable);
}
