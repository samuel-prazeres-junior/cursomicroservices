package io.github.cursodsousa.msclientes.application.controller;

import io.github.cursodsousa.msclientes.application.representation.ClienteSaveRequest;
import io.github.cursodsousa.msclientes.application.service.ClienteService;
import io.github.cursodsousa.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteResource {

    private final ClienteService service;

    @GetMapping("/all")
    public List<Cliente> allClientes(){
        return service.findAll();
    }

    @GetMapping()
    public String status(){
        log.info("obtendo o status do microservice de clientes");
        return "Ok";
    }


    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request){
        var cliente = request.toModel();
        service.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf{cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(@RequestParam("cpf") String cpf){
        var cliente = service.getByCPF(cpf);
        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
