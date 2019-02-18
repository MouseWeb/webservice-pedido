package br.com.mouseweb.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.mouseweb.domain.Cliente;
import br.com.mouseweb.domain.Pedido;

public interface EmailService {

	// Envia o email plano sem o HTML
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);

	// Envia o email com Template HTML
	//void sendHtmlEmail(MimeMessage msg);
	//void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);

}