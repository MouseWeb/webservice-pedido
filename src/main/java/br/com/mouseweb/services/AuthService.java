package br.com.mouseweb.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.mouseweb.domain.Cliente;
import br.com.mouseweb.repositories.ClienteRepository;
import br.com.mouseweb.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailService emailService;
	
	// Random é uma classe do Java que gera números aleatórios
	private Random rand = new Random();

	// Método = Esqueci a senha
	public void sendNewPassword(String email) {
		// Verifica se o (e-mail) existe -> buscar um cliente por email.
		Cliente cliente = clienteRepository.findByEmail(email);
		// Se este email não existe significa que o email não está cadastrado.
		if (cliente == null) {
			throw new ObjectNotFoundException("E-mail não encontrado");
		}

		// Gera uma nova senha para o usuário
		// newPassword() -> método que retona uma senha Aleatória.
		String newPass = newPassword();
		// coloca a nova senha no OBJ cliente
		cliente.setSenha(pe.encode(newPass));

		// Salva o cliente no banco de dados.
		clienteRepository.save(cliente);

		// Envia o email para o cliente -> passando o cliente e a mova senha como
		// argumentos para (sendNewPasswordEmail())
		emailService.sendNewPasswordEmail(cliente, newPass);
	}
	
	// Gera uma senha de 10 Caracteres esses caracteres podem ser digitos ou letras.

	// Método auxiliar que gera uma senha Aleatória  
	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	// Função que gera um caracter aleatório que pode ser digito ou letra
	// Códigos unicode: https://unicode-table.com/pt/#control-character
	private char randomChar() {
		int opt = rand.nextInt(3);
		if (opt == 0) { // gera um digito
			return (char) (rand.nextInt(10) + 48);
		}
		else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { // gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
