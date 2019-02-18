package br.com.mouseweb.domain;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.mouseweb.domain.enums.EstadoPagamento;

@Entity
@JsonTypeName("pagamentoComCartao")
public class PagamentoCartao extends Pagamento{
	
	private static final long serialVersionUID = 1L;
	
	private Integer numeroDeParcelas;
	
	public PagamentoCartao() {
		
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public PagamentoCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
		super(id, estado, pedido);
		this.numeroDeParcelas = numeroDeParcelas;
	}
	
	
}
