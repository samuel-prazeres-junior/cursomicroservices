package io.github.curso.msavaliadorcredito.application.controller;


import feign.FeignException;
import io.github.curso.msavaliadorcredito.application.exception.DadosClientesNotFoundException;
import io.github.curso.msavaliadorcredito.application.exception.ErroComunicacaoMicroservicesException;
import io.github.curso.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import io.github.curso.msavaliadorcredito.application.service.AvalidadorCreditoService;
import io.github.curso.msavaliadorcredito.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes-credito/")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvalidadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "Ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity consultarSituacaoCliente(@RequestParam("cpf") String cpf) {

        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClientesNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClientesNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
