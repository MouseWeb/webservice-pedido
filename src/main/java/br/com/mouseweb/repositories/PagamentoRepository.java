package br.com.mouseweb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mouseweb.domain.Pagamento;;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>{

}
