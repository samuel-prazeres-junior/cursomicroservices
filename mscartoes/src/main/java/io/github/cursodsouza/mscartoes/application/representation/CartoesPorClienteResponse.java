package io.github.cursodsouza.mscartoes.application.representation;

import io.github.cursodsouza.mscartoes.domain.CartaoCliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartoesPorClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromToModel(CartaoCliente model){
        return new CartoesPorClienteResponse(
                model.getCartao().getNome(),
                model.getCartao().getBandeira().toString(),
                model.getLimite());
    }
}
