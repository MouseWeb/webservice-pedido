package br.com.mouseweb.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.mouseweb.domain.Categoria;
import br.com.mouseweb.domain.Produto;;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{
	
	// Método pesonalisado, sem usar a palavra chave. 
	// Usando o @Query generico.
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(
			@Param("nome") String nome, 
			@Param("categorias") 
			List<Categoria> categorias, 
			Pageable pageRequest);
	
	// O springData tem varias palavras rezevadas que já monta a consulta automaticamente 
	//		LINK: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods
	
	// Forma sem @Query, usando as palavras chaves do SpringData, faz a query no banco automaticamente.
	//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	
	// A Query sobre o metodo sobrepoe e executa a @Query.
	//@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
