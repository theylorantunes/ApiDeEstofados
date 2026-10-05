package com.estofados.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.estofados.controller.FabricanteController;
import com.estofados.model.FabricanteModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class FabricanteModelAssembler implements RepresentationModelAssembler<FabricanteModel, EntityModel<FabricanteModel>> {
    @Override
    public EntityModel<FabricanteModel> toModel(FabricanteModel fabricante) {
        return EntityModel.of(fabricante,
                linkTo(methodOn(FabricanteController.class).buscarPorId(fabricante.getFabricanteId())).withSelfRel(),
                linkTo(methodOn(FabricanteController.class).listarFabricantes(null)).withRel("lista_de_fabricantes"));
    }
}