package br.com.generic.service.repository;

import br.com.generic.service.entity.SimulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimulationRepository extends JpaRepository<SimulationEntity, Long> {

    Optional<SimulationEntity> findByCpf(String cpf);





}

