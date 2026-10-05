package com.estofados.model;

import com.estofados.enums.StatusFabricacao;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_estofado")
public class EstofadoModel extends RepresentationModel<EstofadoModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long estofadoId;

    private String nomeModelo;
    private BigDecimal preco;
    private String corPrincipal;

    // enum
    @Enumerated(EnumType.STRING)
    private StatusFabricacao status;

    // um para um
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "especificacao_id", referencedColumnName = "especificacaoId")
    private EspecificacaoModel especificacao;

    // muitos para um
    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    private FabricanteModel fabricante;

    // muitos para muitos
    @ManyToMany
    @JoinTable(
            name = "tb_estofado_material",
            joinColumns = @JoinColumn(name = "estofado_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<MaterialModel> materiais;

    public Long getEstofadoId() { return estofadoId; }
    public void setEstofadoId(Long estofadoId) { this.estofadoId = estofadoId; }

    public String getNomeModelo() { return nomeModelo; }
    public void setNomeModelo(String nomeModelo) { this.nomeModelo = nomeModelo; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public String getCorPrincipal() { return corPrincipal; }
    public void setCorPrincipal(String corPrincipal) { this.corPrincipal = corPrincipal; }

    public StatusFabricacao getStatus() { return status; }
    public void setStatus(StatusFabricacao status) { this.status = status; }

    public EspecificacaoModel getEspecificacao() { return especificacao; }
    public void setEspecificacao(EspecificacaoModel especificacao) { this.especificacao = especificacao; }

    public FabricanteModel getFabricante() { return fabricante; }
    public void setFabricante(FabricanteModel fabricante) { this.fabricante = fabricante; }

    public List<MaterialModel> getMateriais() { return materiais; }
    public void setMateriais(List<MaterialModel> materiais) { this.materiais = materiais; }
}