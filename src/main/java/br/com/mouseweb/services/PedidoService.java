package br.com.mouseweb.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mouseweb.domain.Cliente;
import br.com.mouseweb.domain.ItemPedido;
import br.com.mouseweb.domain.PagamentoBoleto;
import br.com.mouseweb.domain.Pedido;
import br.com.mouseweb.domain.enums.EstadoPagamento;
import br.com.mouseweb.repositories.ClienteRepository;
import br.com.mouseweb.repositories.ItemPedidoRepository;
import br.com.mouseweb.repositories.PagamentoRepository;
import br.com.mouseweb.repositories.PedidoRepository;
import br.com.mouseweb.security.UserSS;
import br.com.mouseweb.services.exceptions.AuthorizationException;
import br.com.mouseweb.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired 
	private ProdutoService produtoService; 
	
	@Autowired

	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoBoleto) {
			PagamentoBoleto pagto = (PagamentoBoleto) obj.getPagamento();
			boletoService.preencherPagamentoBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) { 
			ip.setDesconto(0.0); 
			ip.setProduto(produtoService.find(ip.getProduto().getId())); 
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);

		} 
		
		itemPedidoRepository.saveAll(obj.getItens()); 
		//System.out.println(obj);
		//emailService.sendOrderConfirmationHtmlEmail(obj);
		emailService.sendOrderConfirmationEmail(obj);
		return obj; 
	}
	
	// Consula de pedidos = Restrição de conteúdo: cliente só recupera seus pedidos.
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		// Obtem o usuário logado
		UserSS user = UserService.authenticated();
		// Se o user for igual a null significa que o mesmo não esta autenticado. 
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		// Retorna somente o pedido do cliente logado
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
		
	}

}
