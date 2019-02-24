package br.com.mouseweb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.mouseweb.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{

	@Transactional(readOnly=true)
	// Retorna todos os estados ordenados por nomes.
	public List<Estado> findAllByOrderByNome();

}
