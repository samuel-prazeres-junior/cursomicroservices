package io.github.cursodsouza.mscartoes.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsouza.mscartoes.domain.Cartao;
import io.github.cursodsouza.mscartoes.domain.CartaoCliente;
import io.github.cursodsouza.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import io.github.cursodsouza.mscartoes.infra.repository.CartaoClienteRepository;
import io.github.cursodsouza.mscartoes.infra.repository.CartaoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final CartaoRepository repository;
    private final CartaoClienteRepository cartaoClienteRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String paylod){

        try {
            var mapper = new ObjectMapper();
            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(paylod, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = repository.findById(dados.getIdCartao()).orElseThrow();
            CartaoCliente cartaoCliente = new CartaoCliente();
            cartaoCliente.setCartao(cartao);
            cartaoCliente.setCpf(dados.getCpf());
            cartaoCliente.setLimite(dados.getLimiteLiberado());
            cartaoClienteRepository.save(cartaoCliente);
        }catch (JsonProcessingException e){
            log.error("Erro ao receber solicitacao de emissao de cartao {}", e.getMessage());
        }
    }
}
