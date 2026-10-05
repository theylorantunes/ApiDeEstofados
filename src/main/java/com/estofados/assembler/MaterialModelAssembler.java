package com.estofados.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.estofados.controller.MaterialController;
import com.estofados.model.MaterialModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class MaterialModelAssembler implements RepresentationModelAssembler<MaterialModel, EntityModel<MaterialModel>> {
    @Override
    public EntityModel<MaterialModel> toModel(MaterialModel material) {
        return EntityModel.of(material,
                linkTo(methodOn(MaterialController.class).buscarPorId(material.getMaterialId())).withSelfRel(),
                linkTo(methodOn(MaterialController.class).listarMateriais(null)).withRel("lista_de_materiais"));
    }
}