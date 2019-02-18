package br.com.mouseweb.services;

import org.springframework.security.core.context.SecurityContextHolder;
import br.com.mouseweb.security.UserSS;

public class UserService {

	// Método retorna o suario logado
	// Pega qual é o usuario autenticado no sistema, logado no sistema.
	// authenticated() = retorna qual é o usuário logado no sistema.
	public static UserSS authenticated() {
		try {
			
		return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		} catch (Exception e) {
			return null;
		}
	}
}
