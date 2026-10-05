package com.estofados.repository;

import com.estofados.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
    // busca por nome da categoria
    List<CategoriaModel> findByNomeCategoriaIgnoreCase(String nome);
}