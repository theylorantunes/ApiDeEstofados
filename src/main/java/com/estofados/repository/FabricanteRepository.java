package com.estofados.repository;

import com.estofados.model.FabricanteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FabricanteRepository extends JpaRepository<FabricanteModel, Long> {
    // busca por cidade
    List<FabricanteModel> findByCidadeSedeIgnoreCase(String cidade);
}