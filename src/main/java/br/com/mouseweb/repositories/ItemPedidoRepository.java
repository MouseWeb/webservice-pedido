package br.com.mouseweb.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mouseweb.domain.ItemPedido;;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer>{

}
