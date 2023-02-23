package io.github.cursodsouza.mscartoes.application.controller;

import io.github.cursodsouza.mscartoes.application.representation.CartaoSaveRequest;
import io.github.cursodsouza.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.cursodsouza.mscartoes.application.service.CartaoClienteService;
import io.github.cursodsouza.mscartoes.application.service.CartaoService;
import io.github.cursodsouza.mscartoes.domain.Cartao;
import io.github.cursodsouza.mscartoes.domain.CartaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService;
    private final CartaoClienteService cartaoClienteService;

    @GetMapping()
    public String status(){
        return "Ok";
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody CartaoSaveRequest request){
        cartaoService.salvar(request.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda){
        List<Cartao> listCartao = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(listCartao);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
        List<CartaoCliente> list = cartaoClienteService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = list
                .stream()
                .map(CartoesPorClienteResponse::fromToModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }
}
