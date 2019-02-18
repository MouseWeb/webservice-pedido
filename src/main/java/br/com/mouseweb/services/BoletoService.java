package br.com.mouseweb.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.mouseweb.domain.PagamentoBoleto;

@Service
public class BoletoService {
	
	// Provisorio, futuramente fazer a chamada de um webservice para pegar a data de vencimento do boleto.
	public void preencherPagamentoBoleto(PagamentoBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
