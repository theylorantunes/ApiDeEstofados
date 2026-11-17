package com.estofados.controller;

import com.estofados.assembler.MaterialModelAssembler;
import com.estofados.exception.MaterialNotFoundException;
import com.estofados.controller.dto.MaterialRecordDto;
import com.estofados.model.MaterialModel;
import com.estofados.repository.MaterialRepository;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/materiais")
@Tag(name = "4. Materiais", description = "Componentes e revestimentos dos móveis")
public class MaterialController {

    @Autowired MaterialRepository repository;
    @Autowired MaterialModelAssembler assembler;

    @Operation(summary = "Criar material")
    @PostMapping
    public ResponseEntity<?> salvarMaterial(@RequestBody @Valid MaterialRecordDto dto) {
        var modelo = new MaterialModel();
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<MaterialModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @Operation(summary = "Listar todos os materiais de forma paginada")
    @GetMapping
    public ResponseEntity<Page<EntityModel<MaterialModel>>> listarMateriais(
            @PageableDefault(size = 10, sort = {"nomeMaterial"}) Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).map(assembler::toModel));
    }

    @Operation(summary = "Busca um material específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Material não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<MaterialModel> buscarPorId(@PathVariable(value = "id") Long id) {
        return assembler.toModel(repository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id)));
    }

    @Operation(summary = "Atualiza um material")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMaterial(@PathVariable(value = "id") Long id, @RequestBody @Valid MaterialRecordDto dto) {
        var modelo = repository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id));
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<MaterialModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Deleta um material")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMaterial(@PathVariable(value = "id") Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca materiais pelo tipo da parte")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<EntityModel<MaterialModel>>> buscarPorTipo(@PathVariable(value = "tipo") String tipo) {
        return ResponseEntity.ok(repository.findByTipoParteIgnoreCase(tipo).stream().map(assembler::toModel).toList());
    }
}