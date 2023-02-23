package io.github.cursodsouza.mscartoes.application.representation;

import io.github.cursodsouza.mscartoes.domain.enums.BandeiraCartao;
import io.github.cursodsouza.mscartoes.domain.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel(){
        return new Cartao(nome, bandeira, renda, limite);
    }
}
