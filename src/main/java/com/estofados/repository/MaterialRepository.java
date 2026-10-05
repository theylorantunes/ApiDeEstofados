package com.estofados.repository;

import com.estofados.model.MaterialModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialModel, Long> {
    // busca pelo tipo da parte
    List<MaterialModel> findByTipoParteIgnoreCase(String tipo);
}