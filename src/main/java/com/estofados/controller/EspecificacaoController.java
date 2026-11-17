package com.estofados.controller;

import com.estofados.assembler.EspecificacaoModelAssembler;
import com.estofados.exception.EspecificacaoNotFoundException;
import com.estofados.controller.dto.EspecificacaoRecordDto;
import com.estofados.model.EspecificacaoModel;
import com.estofados.repository.EspecificacaoRepository;
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
@RequestMapping("/especificacoes")
@Tag(name = "5. Especificações Técnicas", description = "Fichas técnicas, dimensões e limites")
public class EspecificacaoController {

    @Autowired EspecificacaoRepository repository;
    @Autowired EspecificacaoModelAssembler assembler;

    @Operation(summary = "Criar ficha técnica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Especificação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @PostMapping
    public ResponseEntity<?> salvarEspecificacao(@RequestBody @Valid EspecificacaoRecordDto dto) {
        var modelo = new EspecificacaoModel();
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<EspecificacaoModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @Operation(summary = "Listar todas as especificações de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping
    public ResponseEntity<Page<EntityModel<EspecificacaoModel>>> listarEspecificacoes(
            @PageableDefault(size = 10, sort = {"especificacaoId"}) Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).map(assembler::toModel));
    }

    @Operation(summary = "Busca uma especificação específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especificação encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Especificação não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<EspecificacaoModel> buscarPorId(@PathVariable(value = "id") Long id) {
        return assembler.toModel(repository.findById(id).orElseThrow(() -> new EspecificacaoNotFoundException(id)));
    }

    @Operation(summary = "Atualiza ficha técnica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especificação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Especificação não encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEspecificacao(@PathVariable(value = "id") Long id, @RequestBody @Valid EspecificacaoRecordDto dto) {
        var modelo = repository.findById(id).orElseThrow(() -> new EspecificacaoNotFoundException(id));
        BeanUtils.copyProperties(dto, modelo);
        EntityModel<EspecificacaoModel> entityModel = assembler.toModel(repository.save(modelo));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Deleta ficha técnica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especificação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Especificação não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEspecificacao(@PathVariable(value = "id") Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new EspecificacaoNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca especificações pela densidade da espuma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping("/densidade/{densidade}")
    public ResponseEntity<List<EntityModel<EspecificacaoModel>>> buscarPorDensidade(@PathVariable(value = "densidade") String densidade) {
        return ResponseEntity.ok(repository.findByDensidadeEspumaIgnoreCase(densidade).stream().map(assembler::toModel).toList());
    }
}