package com.estofados.controller.v2;

import com.estofados.exception.EstofadoNotFoundException;
import com.estofados.model.EstofadoModel;
import com.estofados.repository.*;
import com.estofados.security.IdempotencyFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Hidden;

import java.net.URI;

@RestController
@RequestMapping("/estofados")
@Tag(name = "1. Estofados (V2)", description = "Gestão do acervo - Versão 2 (Exige header X-API-Version: 2)")
@Hidden
public class EstofadoV2Controller {

    private final EstofadoRepository repository;
    private final FabricanteRepository fabRepo;
    private final EspecificacaoRepository espRepo;
    private final MaterialRepository matRepo;

    public EstofadoV2Controller(EstofadoRepository repository, FabricanteRepository fabRepo, EspecificacaoRepository espRepo, MaterialRepository matRepo) {
        this.repository = repository; this.fabRepo = fabRepo; this.espRepo = espRepo; this.matRepo = matRepo;
    }

    @Operation(summary = "Listar todos os estofados (v2)")
    @GetMapping(version = "2")
    public PagedModel<EstofadoResponse> getAll(
            @PageableDefault(size = 10, sort = "estofadoId") Pageable pageable) {
        return new PagedModel<>(repository.findAll(pageable).map(EstofadoResponse::from));
    }

    @Operation(summary = "Criar estofado (v2)")
    @ApiResponse(responseCode = "201", description = "Estofado criado")
    @ApiResponse(responseCode = "409", description = "Requisição com mesma chave em andamento", content = @Content)
    @ApiResponse(responseCode = "422", description = "Chave já usada com corpo diferente", content = @Content)
    @Parameter(in = ParameterIn.HEADER, name = IdempotencyFilter.HEADER, schema = @Schema(type = "string"))
    @PostMapping(version = "2")
    public ResponseEntity<EstofadoResponse> newEstofado(@RequestBody @Valid EstofadoRequest request) {
        var fab = fabRepo.findById(request.fabricanteId()).orElseThrow();
        var esp = espRepo.findById(request.especificacaoId()).orElseThrow();
        var mat = matRepo.findAllById(request.materiaisIds());

        EstofadoModel model = new EstofadoModel();
        model.setNomeModelo(request.nomeModelo());
        model.setPreco(request.detalhes().preco());
        model.setCorPrincipal(request.detalhes().corPrincipal());
        model.setStatus(request.status());
        model.setFabricante(fab);
        model.setEspecificacao(esp);
        model.setMateriais(mat);

        EstofadoModel saved = repository.save(model);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(saved.getEstofadoId()).toUri();
        return ResponseEntity.created(location).body(EstofadoResponse.from(saved));
    }
}