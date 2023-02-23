package io.github.cursodsouza.mscartoes.application.service;

import io.github.cursodsouza.mscartoes.domain.CartaoCliente;
import io.github.cursodsouza.mscartoes.infra.repository.CartaoClienteRepository;
import io.github.cursodsouza.mscartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoClienteService {

    private final CartaoClienteRepository repository;

    public List<CartaoCliente> listCartoesByCpf(String cpf){
        return repository.findByCpf(cpf);
    }
}
