package com.estofados.controller;

import com.estofados.controller.dto.FabricanteRecordDto;
import com.estofados.exception.CategoriaNotFoundException;
import com.estofados.assembler.FabricanteModelAssembler;
import com.estofados.exception.FabricanteNotFoundException;
import com.estofados.model.FabricanteModel;
import com.estofados.repository.CategoriaRepository;
import com.estofados.repository.FabricanteRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/fabricantes")
public class FabricanteController {

    @Autowired FabricanteRepository repository;
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired FabricanteModelAssembler assembler;

    @Hidden
    @PostMapping
    public ResponseEntity<?> salvarFabricante(@RequestBody @Valid FabricanteRecordDto dto) {
        var categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException(dto.categoriaId()));
        var modelo = new FabricanteModel();
        BeanUtils.copyProperties(dto, modelo);
        modelo.setCategoria(categoria);

        EntityModel<FabricanteModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @Operation(summary = "Listar todos os fabricantes de forma paginada")
    @GetMapping
    public ResponseEntity<Page<EntityModel<FabricanteModel>>> listarFabricantes(
            @Parameter(description = "Configurações de paginação")
            @PageableDefault(size = 10, sort = {"nomeFabricante"}) Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).map(assembler::toModel));
    }

    @Operation(summary = "Busca um fabricante específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fabricante encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fabricante não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<FabricanteModel> buscarPorId(@PathVariable(value = "id") Long id) {
        return assembler.toModel(repository.findById(id).orElseThrow(() -> new FabricanteNotFoundException(id)));
    }

    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFabricante(@PathVariable(value = "id") Long id, @RequestBody @Valid FabricanteRecordDto dto) {
        var modelo = repository.findById(id).orElseThrow(() -> new FabricanteNotFoundException(id));
        var categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException(dto.categoriaId()));

        BeanUtils.copyProperties(dto, modelo);
        modelo.setCategoria(categoria);

        EntityModel<FabricanteModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarFabricante(@PathVariable(value = "id") Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new FabricanteNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca fabricantes por cidade")
    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<EntityModel<FabricanteModel>>> buscarPorCidade(@PathVariable(value = "cidade") String cidade) {
        return ResponseEntity.ok(repository.findByCidadeSedeIgnoreCase(cidade).stream().map(assembler::toModel).toList());
    }
}