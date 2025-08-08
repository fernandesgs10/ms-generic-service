package br.com.generic.service.repository;

import br.com.generic.service.entity.SimulationItensEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationItensRepository extends JpaRepository<SimulationItensEntity, Long> {

    Page<SimulationItensEntity> findBySimulationId(Long simulationId, Pageable pageable);



}

