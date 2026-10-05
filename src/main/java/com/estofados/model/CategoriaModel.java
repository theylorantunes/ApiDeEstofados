package com.estofados.model;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "tb_categoria")
public class CategoriaModel extends RepresentationModel<CategoriaModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoriaId;

    private String nomeCategoria;
    private String descricaoUso;

    // getters e setters
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }

    public String getDescricaoUso() { return descricaoUso; }
    public void setDescricaoUso(String descricaoUso) { this.descricaoUso = descricaoUso; }
}