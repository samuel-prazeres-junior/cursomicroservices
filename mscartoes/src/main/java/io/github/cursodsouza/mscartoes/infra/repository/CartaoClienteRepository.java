package io.github.cursodsouza.mscartoes.infra.repository;

import io.github.cursodsouza.mscartoes.domain.CartaoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoClienteRepository extends JpaRepository<CartaoCliente, Long> {
    List<CartaoCliente> findByCpf(String cpf);
}
