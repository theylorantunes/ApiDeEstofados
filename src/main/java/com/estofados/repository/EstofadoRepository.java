package com.estofados.repository;

import com.estofados.model.EstofadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstofadoRepository extends JpaRepository<EstofadoModel, Long> {
    // busca por nome do modelo
    List<EstofadoModel> findByNomeModeloContainingIgnoreCase(String nome);
}