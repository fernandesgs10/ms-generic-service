package br.com.generic.service.mapper;

import br.com.generic.service.account.SimulationResponse;
import br.com.generic.service.entity.SimulationItensEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@Component
@AllArgsConstructor
public class SimulationItensMapper implements Serializable {

    protected final ModelMapper modelMapper;

    public SimulationItensEntity converterToEntity(SimulationResponse simulationResponse) {
        return modelMapper.map(simulationResponse, SimulationItensEntity.class);
    }

    public SimulationResponse converterToResponse(SimulationItensEntity simulationItensEntity) {
        return modelMapper.map(simulationItensEntity, SimulationResponse.class);
    }


}