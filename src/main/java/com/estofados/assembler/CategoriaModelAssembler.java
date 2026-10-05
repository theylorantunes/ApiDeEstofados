package com.estofados.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.estofados.controller.CategoriaController;
import com.estofados.model.CategoriaModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaModel, EntityModel<CategoriaModel>> {
    @Override
    public EntityModel<CategoriaModel> toModel(CategoriaModel categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).buscarPorId(categoria.getCategoriaId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).listarCategorias(null)).withRel("lista_de_categorias"));
    }
}