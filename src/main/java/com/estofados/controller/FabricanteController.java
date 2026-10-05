package com.estofados.controller;

import com.estofados.*;
import com.estofados.controller.dto.FabricanteRecordDto;
import com.estofados.exception.CategoriaNotFoundException;
import com.estofados.assembler.FabricanteModelAssembler;
import com.estofados.exception.FabricanteNotFoundException;
import com.estofados.model.FabricanteModel;
import com.estofados.repository.CategoriaRepository;
import com.estofados.repository.FabricanteRepository;
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

    // criar
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

    @GetMapping
    public ResponseEntity<Page<EntityModel<FabricanteModel>>> listarFabricantes(@PageableDefault(size = 10, sort = {"nomeFabricante"}) Pageable pageable) {
        return ResponseEntity.ok(repository.findAll(pageable).map(assembler::toModel));
    }

    @GetMapping("/{id}")
    public EntityModel<FabricanteModel> buscarPorId(@PathVariable(value = "id") Long id) {
        return assembler.toModel(repository.findById(id).orElseThrow(() -> new FabricanteNotFoundException(id)));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarFabricante(@PathVariable(value = "id") Long id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new FabricanteNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<EntityModel<FabricanteModel>>> buscarPorCidade(@PathVariable(value = "cidade") String cidade) {
        return ResponseEntity.ok(repository.findByCidadeSedeIgnoreCase(cidade).stream().map(assembler::toModel).toList());
    }
}