package com.estofados.controller;

import com.estofados.assembler.EstofadoModelAssembler;
import com.estofados.exception.EstofadoNotFoundException;
import com.estofados.controller.dto.EstofadoRecordDto;
import com.estofados.model.EstofadoModel;
import com.estofados.repository.EspecificacaoRepository;
import com.estofados.repository.EstofadoRepository;
import com.estofados.repository.FabricanteRepository;
import com.estofados.repository.MaterialRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/estofados")
public class EstofadoController {

    @Autowired
    EstofadoRepository estofadoRepository;

    @Autowired
    FabricanteRepository fabricanteRepository;

    @Autowired
    EspecificacaoRepository especificacaoRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    EstofadoModelAssembler assembler;

    @Tag(name = "1. Catálogo de Estofados", description = "Gestão do acervo principal de produtos")
    @PostMapping
    public ResponseEntity<?> salvarEstofado(@RequestBody @Valid EstofadoRecordDto dto) {
        var fabricante = fabricanteRepository.findById(dto.fabricanteId())
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado."));
        var especificacao = especificacaoRepository.findById(dto.especificacaoId())
                .orElseThrow(() -> new RuntimeException("Especificação não encontrada."));
        var materiais = materialRepository.findAllById(dto.materiaisIds());

        var estofadoModel = new EstofadoModel();
        BeanUtils.copyProperties(dto, estofadoModel);

        estofadoModel.setFabricante(fabricante);
        estofadoModel.setEspecificacao(especificacao);
        estofadoModel.setMateriais(materiais);

        EntityModel<EstofadoModel> entityModel = assembler.toModel(estofadoRepository.save(estofadoModel));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Listar todos os estofados de forma paginada")
    @GetMapping
    public ResponseEntity<Page<EntityModel<EstofadoModel>>> listarEstofados(
            @Parameter(description = "Configurações de paginação")
            @PageableDefault(size = 10, sort = {"nomeModelo"}) Pageable pageable) {
        Page<EstofadoModel> estofados = estofadoRepository.findAll(pageable);
        return ResponseEntity.ok(estofados.map(assembler::toModel));
    }

    @Operation(summary = "Busca um estofado específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estofado encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estofado não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<EstofadoModel> buscarPorId(@PathVariable(value = "id") Long id) {
        EstofadoModel estofado = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        return assembler.toModel(estofado);
    }

    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEstofado(@PathVariable(value = "id") Long id, @RequestBody @Valid EstofadoRecordDto dto) {
        EstofadoModel estofadoModel = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        var fabricante = fabricanteRepository.findById(dto.fabricanteId())
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado."));
        var especificacao = especificacaoRepository.findById(dto.especificacaoId())
                .orElseThrow(() -> new RuntimeException("Especificação não encontrada."));
        var materiais = materialRepository.findAllById(dto.materiaisIds());

        BeanUtils.copyProperties(dto, estofadoModel);
        estofadoModel.setFabricante(fabricante);
        estofadoModel.setEspecificacao(especificacao);
        estofadoModel.setMateriais(materiais);

        EntityModel<EstofadoModel> entityModel = assembler.toModel(estofadoRepository.save(estofadoModel));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEstofado(@PathVariable(value = "id") Long id) {
        EstofadoModel estofado = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        estofadoRepository.delete(estofado);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca estofados pelo modelo")
    @GetMapping("/modelo/{nome}")
    public ResponseEntity<List<EntityModel<EstofadoModel>>> buscarPorModelo(@PathVariable(value = "nome") String nome) {
        List<EntityModel<EstofadoModel>> lista = estofadoRepository.findByNomeModeloContainingIgnoreCase(nome)
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(lista);
    }
}