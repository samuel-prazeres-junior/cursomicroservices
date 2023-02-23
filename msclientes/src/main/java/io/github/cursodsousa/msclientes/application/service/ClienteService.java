package io.github.cursodsousa.msclientes.application.service;

import io.github.cursodsousa.msclientes.domain.Cliente;
import io.github.cursodsousa.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente save(Cliente cliente){
        return repository.save(cliente);
    }

    public Optional<Cliente> getByCPF(String cpf){
        return repository.findByCpf(cpf);
    }


    public List<Cliente> findAll() {
        return repository.findAll();
    }
}
