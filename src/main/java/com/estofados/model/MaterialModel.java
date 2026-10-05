package com.estofados.model;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "tb_material")
public class MaterialModel extends RepresentationModel<MaterialModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long materialId;

    private String nomeMaterial;
    private String tipoParte;

    // getters e setters
    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public String getNomeMaterial() { return nomeMaterial; }
    public void setNomeMaterial(String nomeMaterial) { this.nomeMaterial = nomeMaterial; }

    public String getTipoParte() { return tipoParte; }
    public void setTipoParte(String tipoParte) { this.tipoParte = tipoParte; }
}