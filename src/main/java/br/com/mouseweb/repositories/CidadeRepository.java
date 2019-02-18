package br.com.mouseweb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mouseweb.domain.Cidade;;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

}
