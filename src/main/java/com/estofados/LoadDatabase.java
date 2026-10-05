package com.estofados;

import com.estofados.enums.StatusFabricacao;
import com.estofados.model.*;
import com.estofados.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EstofadoRepository estofadoRepository,
                                   FabricanteRepository fabricanteRepository,
                                   CategoriaRepository categoriaRepository,
                                   MaterialRepository materialRepository) {
        return args -> {
            // salva categoria
            CategoriaModel sala = new CategoriaModel();
            sala.setNomeCategoria("sala de estar");
            sala.setDescricaoUso("uso interno residencial");
            categoriaRepository.save(sala);

            // salva fabricante
            FabricanteModel herval = new FabricanteModel();
            herval.setNomeFabricante("herval");
            herval.setCnpj("12345678901234");
            herval.setCidadeSede("dois irmaos");
            herval.setCategoria(sala);
            fabricanteRepository.save(herval);

            // salva materiais
            MaterialModel linho = new MaterialModel();
            linho.setNomeMaterial("linho cru");
            linho.setTipoParte("revestimento");
            materialRepository.save(linho);

            MaterialModel madeira = new MaterialModel();
            madeira.setNomeMaterial("eucalipto");
            madeira.setTipoParte("estrutura");
            materialRepository.save(madeira);

            // cria especificacao
            EspecificacaoModel espec = new EspecificacaoModel();
            espec.setDimensoes("2.20m x 0.90m");
            espec.setPesoSuportadoKg(300);
            espec.setDensidadeEspuma("d-33");

            // salva estofado amarrando tudo
            EstofadoModel sofa = new EstofadoModel();
            sofa.setNomeModelo("sofa retratil master");
            sofa.setPreco(new BigDecimal("3500.00"));
            sofa.setCorPrincipal("bege");
            sofa.setStatus(StatusFabricacao.DE_LINHA);
            sofa.setFabricante(herval);
            sofa.setEspecificacao(espec);
            sofa.setMateriais(Arrays.asList(linho, madeira));

            log.info("carregando estofado de teste: " + estofadoRepository.save(sofa));
        };
    }
}