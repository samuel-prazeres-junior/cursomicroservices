package io.github.curso.msavaliadorcredito.application.service;

import feign.FeignException;
import io.github.curso.msavaliadorcredito.application.exception.DadosClientesNotFoundException;
import io.github.curso.msavaliadorcredito.application.exception.ErroComunicacaoMicroservicesException;
import io.github.curso.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import io.github.curso.msavaliadorcredito.domain.model.*;
import io.github.curso.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.curso.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.curso.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvalidadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;
    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClientesNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesResourceClient.getCartoesByCliente(cpf);
            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClientesNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao (String cpf, Long renda) throws DadosClientesNotFoundException, ErroComunicacaoMicroservicesException{
        try {

            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartaoResponse = cartoesResourceClient.getCartoesRendaAteh(renda);
            List<Cartao> cartoes = cartaoResponse.getBody();
            var listCartoesAprovados = cartoes.stream().map(cartao -> {

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosClienteResponse.getBody().getIdade());
                var fator =idadeBD.divide(BigDecimal.valueOf(10));

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(fator.multiply(limiteBasico));

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listCartoesAprovados);

        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClientesNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
