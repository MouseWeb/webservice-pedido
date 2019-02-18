package br.com.mouseweb.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.mouseweb.domain.Categoria;
import br.com.mouseweb.domain.Produto;
import br.com.mouseweb.repositories.CategoriaRepository;
import br.com.mouseweb.repositories.ProdutoRepository;
import br.com.mouseweb.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	// Busca por paginacao, usando @Query -> Inner JOIR
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		List<Categoria> categorias = categoriaRepository.findAllById(ids); 
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		
		return repo.search(nome, categorias, pageRequest);
	}

}
