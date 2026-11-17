package com.estofados.controller.v2;
import com.estofados.model.EstofadoModel;

public record EstofadoResponse(Long id, String nomeModelo, Detalhes detalhes, String status) {
    public static EstofadoResponse from(EstofadoModel model) {
        return new EstofadoResponse(
                model.getEstofadoId(),
                model.getNomeModelo(),
                new Detalhes(model.getPreco(), model.getCorPrincipal()),
                model.getStatus().name()
        );
    }
}