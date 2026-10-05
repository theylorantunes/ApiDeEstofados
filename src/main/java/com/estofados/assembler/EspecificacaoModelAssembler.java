package com.estofados.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.estofados.controller.EspecificacaoController;
import com.estofados.model.EspecificacaoModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EspecificacaoModelAssembler implements RepresentationModelAssembler<EspecificacaoModel, EntityModel<EspecificacaoModel>> {
    @Override
    public EntityModel<EspecificacaoModel> toModel(EspecificacaoModel especificacao) {
        return EntityModel.of(especificacao,
                linkTo(methodOn(EspecificacaoController.class).buscarPorId(especificacao.getEspecificacaoId())).withSelfRel(),
                linkTo(methodOn(EspecificacaoController.class).listarEspecificacoes(null)).withRel("lista_de_especificacoes"));
    }
}