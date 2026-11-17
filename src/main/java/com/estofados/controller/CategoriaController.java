package com.estofados.controller;

import com.estofados.assembler.CategoriaModelAssembler;
import com.estofados.exception.CategoriaNotFoundException;
import com.estofados.controller.dto.CategoriaRecordDto;
import com.estofados.model.CategoriaModel;
import com.estofados.repository.CategoriaRepository;
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
@RequestMapping("/categorias")
@Tag(name = "2. Categorias", description = "Classificação de ambientes e uso")
public class CategoriaController {

    @Autowired CategoriaRepository repository;
    @Autowired CategoriaModelAssembler assembler;

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado (Chave de API ausente ou inválida)")
    })
    @PostMapping
    public ResponseEntity<?> salvarCategoria(@RequestBody @Valid CategoriaRecordDto dto) {
        var modelo = new CategoriaModel();
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<CategoriaModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @Operation(summary = "Listar todas as categorias de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping
    public ResponseEntity<Page<EntityModel<CategoriaModel>>> listarCategorias(
            @PageableDefault(size = 10, sort = {"nomeCategoria"}) Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).map(assembler::toModel));
    }

    @Operation(summary = "Busca uma categoria específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<CategoriaModel> buscarPorId(@PathVariable(value = "id") Long id) {
        return assembler.toModel(repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id)));
    }

    @Operation(summary = "Atualiza uma categoria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable(value = "id") Long id, @RequestBody @Valid CategoriaRecordDto dto) {
        var modelo = repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<CategoriaModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Deleta uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso (Sem conteúdo)"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCategoria(@PathVariable(value = "id") Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca categorias pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<EntityModel<CategoriaModel>>> buscarPorNome(@PathVariable(value = "nome") String nome) {
        return ResponseEntity.ok(repository.findByNomeCategoriaIgnoreCase(nome).stream().map(assembler::toModel).toList());
    }
}