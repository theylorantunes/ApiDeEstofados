package com.estofados.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Entity
@Table(name = "tb_fabricante")
public class FabricanteModel extends RepresentationModel<FabricanteModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fabricanteId;

    private String nomeFabricante;
    private String cnpj;
    private String cidadeSede;


    // um para muitos
    @JsonIgnore
    @OneToMany(mappedBy = "fabricante")
    private List<EstofadoModel> estofados;


    // muitos para um (quinta entidade)
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaModel categoria;

    public Long getFabricanteId() { return fabricanteId; }
    public void setFabricanteId(Long fabricanteId) { this.fabricanteId = fabricanteId; }

    public String getNomeFabricante() { return nomeFabricante; }
    public void setNomeFabricante(String nomeFabricante) { this.nomeFabricante = nomeFabricante; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getCidadeSede() { return cidadeSede; }
    public void setCidadeSede(String cidadeSede) { this.cidadeSede = cidadeSede; }

    public List<EstofadoModel> getEstofados() { return estofados; }
    public void setEstofados(List<EstofadoModel> estofados) { this.estofados = estofados; }

    public CategoriaModel getCategoria() { return categoria; }
    public void setCategoria(CategoriaModel categoria) { this.categoria = categoria; }
}