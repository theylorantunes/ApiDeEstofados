package com.estofados.model;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "tb_especificacao")
public class EspecificacaoModel extends RepresentationModel<EspecificacaoModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long especificacaoId;

    private String dimensoes;
    private Integer pesoSuportadoKg;
    private String densidadeEspuma;

    // getters e setters
    public Long getEspecificacaoId() { return especificacaoId; }
    public void setEspecificacaoId(Long especificacaoId) { this.especificacaoId = especificacaoId; }

    public String getDimensoes() { return dimensoes; }
    public void setDimensoes(String dimensoes) { this.dimensoes = dimensoes; }

    public Integer getPesoSuportadoKg() { return pesoSuportadoKg; }
    public void setPesoSuportadoKg(Integer pesoSuportadoKg) { this.pesoSuportadoKg = pesoSuportadoKg; }

    public String getDensidadeEspuma() { return densidadeEspuma; }
    public void setDensidadeEspuma(String densidadeEspuma) { this.densidadeEspuma = densidadeEspuma; }
}