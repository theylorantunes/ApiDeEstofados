package com.estofados.controller;

import com.estofados.assembler.EstofadoModelAssembler;
import com.estofados.exception.EstofadoNotFoundException;
import com.estofados.controller.dto.EstofadoRecordDto;
import com.estofados.model.EstofadoModel;
import com.estofados.repository.EspecificacaoRepository;
import com.estofados.repository.EstofadoRepository;
import com.estofados.repository.FabricanteRepository;
import com.estofados.repository.MaterialRepository;
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
@RequestMapping("/estofados")
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

    // 1 post
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

    // 2 get
    @GetMapping
    public ResponseEntity<Page<EntityModel<EstofadoModel>>> listarEstofados(@PageableDefault(size = 10, sort = {"nomeModelo"}) Pageable pageable) {
        Page<EstofadoModel> estofados = estofadoRepository.findAll(pageable);
        return ResponseEntity.ok(estofados.map(assembler::toModel));
    }

    // 3 get por id
    @GetMapping("/{id}")
    public EntityModel<EstofadoModel> buscarPorId(@PathVariable(value = "id") Long id) {
        EstofadoModel estofado = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        return assembler.toModel(estofado);
    }

    // 4 atualizar
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

    // 5 delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEstofado(@PathVariable(value = "id") Long id) {
        EstofadoModel estofado = estofadoRepository.findById(id)
                .orElseThrow(() -> new EstofadoNotFoundException(id));

        estofadoRepository.delete(estofado);
        return ResponseEntity.noContent().build();
    }

    // 6 consulta por modelo
    @GetMapping("/modelo/{nome}")
    public ResponseEntity<List<EntityModel<EstofadoModel>>> buscarPorModelo(@PathVariable(value = "nome") String nome) {
        List<EntityModel<EstofadoModel>> lista = estofadoRepository.findByNomeModeloContainingIgnoreCase(nome)
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(lista);
    }
}