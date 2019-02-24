package br.com.mouseweb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mouseweb.domain.Cidade;
import br.com.mouseweb.repositories.CidadeRepository;

@Service
public class CidadeService {
 
	@Autowired
	private CidadeRepository repo;

	public List<Cidade> findByEstado(Integer estadoId) {
		return repo.findCidades(estadoId);

	}

}