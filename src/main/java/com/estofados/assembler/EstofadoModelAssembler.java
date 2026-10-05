package com.estofados.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.estofados.controller.EstofadoController;
import com.estofados.model.EstofadoModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EstofadoModelAssembler implements RepresentationModelAssembler<EstofadoModel, EntityModel<EstofadoModel>> {

    @Override
    public EntityModel<EstofadoModel> toModel(EstofadoModel estofado) {
        return EntityModel.of(estofado,
                linkTo(methodOn(EstofadoController.class).buscarPorId(estofado.getEstofadoId())).withSelfRel(),
                linkTo(methodOn(EstofadoController.class).listarEstofados(null)).withRel("lista_de_estofados"));
    }
}