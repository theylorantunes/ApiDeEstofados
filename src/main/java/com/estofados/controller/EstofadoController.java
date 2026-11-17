package com.estofados.controller;

import com.estofados.assembler.EstofadoModelAssembler;
import com.estofados.controller.dto.EstofadoRecordDto;
import com.estofados.exception.EspecificacaoNotFoundException;
import com.estofados.exception.EstofadoNotFoundException;
import com.estofados.exception.FabricanteNotFoundException;
import com.estofados.model.EstofadoModel;
import com.estofados.repository.EspecificacaoRepository;
import com.estofados.repository.EstofadoRepository;
import com.estofados.repository.FabricanteRepository;
import com.estofados.repository.MaterialRepository;
import com.estofados.security.IdempotencyFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/estofados")
@Tag(name = "1. Catálogo de Estofados", description = "Gestão do acervo principal de produtos")
public class EstofadoController {

    @Autowired EstofadoRepository estofadoRepository;
    @Autowired FabricanteRepository fabricanteRepository;
    @Autowired EspecificacaoRepository especificacaoRepository;
    @Autowired MaterialRepository materialRepository;
    @Autowired EstofadoModelAssembler assembler;

    @Operation(summary = "Cadastra um novo estofado", description = "Cria um novo registro de estofado no catálogo. Exige cabeçalho de idempotência.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estofado criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Chave de API inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito: Chave de idempotência já processada"),
            @ApiResponse(responseCode = "429", description = "Muitas requisições")
    })
    @Parameter(in = ParameterIn.HEADER, name = IdempotencyFilter.HEADER, required = true, description = "Chave de idempotência", schema = @Schema(type = "string"))
    @PostMapping
    public ResponseEntity<?> salvarEstofado(
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody @Valid EstofadoRecordDto dto) {


        var fabricante = fabricanteRepository.findById(dto.fabricanteId())
                .orElseGet(() -> fabricanteRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new FabricanteNotFoundException(dto.fabricanteId())));

        var especificacao = especificacaoRepository.findById(dto.especificacaoId())
                .orElseGet(() -> especificacaoRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new EspecificacaoNotFoundException(dto.especificacaoId())));

        var materiais = materialRepository.findAllById(dto.materiaisIds());

        var estofadoModel = new EstofadoModel();
        estofadoModel.setNomeModelo(dto.nomeModelo());
        estofadoModel.setPreco(dto.preco());
        estofadoModel.setCorPrincipal(dto.corPrincipal());
        estofadoModel.setStatus(dto.status());
        estofadoModel.setFabricante(fabricante);
        estofadoModel.setEspecificacao(especificacao);
        estofadoModel.setMateriais(materiais);

        EntityModel<EstofadoModel> entityModel = assembler.toModel(estofadoRepository.save(estofadoModel));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Atualiza um estofado existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEstofado(@PathVariable(value = "id") Long id, @RequestBody @Valid EstofadoRecordDto dto) {
        EstofadoModel estofadoModel = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        var fabricante = fabricanteRepository.findById(dto.fabricanteId())
                .orElseThrow(() -> new FabricanteNotFoundException(dto.fabricanteId()));
        var especificacao = especificacaoRepository.findById(dto.especificacaoId())
                .orElseThrow(() -> new EspecificacaoNotFoundException(dto.especificacaoId()));
        var materiais = materialRepository.findAllById(dto.materiaisIds());

        estofadoModel.setNomeModelo(dto.nomeModelo());
        estofadoModel.setPreco(dto.preco());
        estofadoModel.setCorPrincipal(dto.corPrincipal());
        estofadoModel.setStatus(dto.status());
        estofadoModel.setFabricante(fabricante);
        estofadoModel.setEspecificacao(especificacao);
        estofadoModel.setMateriais(materiais);

        EntityModel<EstofadoModel> entityModel = assembler.toModel(estofadoRepository.save(estofadoModel));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Listar todos os estofados de forma paginada")
    @Parameter(in = ParameterIn.HEADER, name = "X-API-Version", description = "Versão da API (use 1 para V1 ou 2 para V2)", schema = @Schema(type = "string", allowableValues = {"1", "2"}, defaultValue = "1"))
    @GetMapping
    public ResponseEntity<Page<EntityModel<EstofadoModel>>> listarEstofados(
            @PageableDefault(size = 10, sort = {"nomeModelo"}) Pageable pageable) {
        Page<EstofadoModel> estofados = estofadoRepository.findAll(pageable);
        return ResponseEntity.ok(estofados.map(assembler::toModel));
    }

    @Operation(summary = "Busca um estofado específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estofado encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estofado não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<EstofadoModel> buscarPorId(@PathVariable(value = "id") Long id) {
        EstofadoModel estofado = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));
        return assembler.toModel(estofado);
    }

    @Operation(summary = "Exclui um estofado do catálogo")
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