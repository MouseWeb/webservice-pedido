package br.com.mouseweb.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import br.com.mouseweb.domain.Cliente;
import br.com.mouseweb.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		// prepareSimpleMailMessageFromPedido = método auxilia que prepara o e-mail
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);

	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		// Destinatario do email
		sm.setTo(obj.getCliente().getEmail());
		// Remetente do email
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;

	}

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		// prepareNewPasswordEmail() = método auxilia que prepara o e-mail
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(sm);

	}

	// protected = da a posibilidade de uma sub-class sobrepo o método
	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);

		return sm;

	}

}
