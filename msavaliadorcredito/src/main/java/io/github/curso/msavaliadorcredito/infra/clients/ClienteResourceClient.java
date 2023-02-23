package io.github.curso.msavaliadorcredito.infra.clients;

import feign.Headers;
import io.github.curso.msavaliadorcredito.domain.model.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "http://localhost:8080", path = "/clientes") //passando a url do microservico
@FeignClient(name = "msclientes", path = "/clientes") // como não está passando url, o spring tentara pegar o nome do microservico via load-balance
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> dadosCliente(@RequestParam("cpf") String cpf);
}
