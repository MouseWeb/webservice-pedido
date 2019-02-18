package br.com.mouseweb.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.mouseweb.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	// Usando a palavra chave (findByEmail).
	// @Transactional = reduz o loc. 
	@Transactional(readOnly=true)
	// Busca no banco de dados um email passando um email por agurmento.
	Cliente findByEmail(String email);
}
