package br.com.mouseweb.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.mouseweb.dto.EmailDTO;
import br.com.mouseweb.security.JWTUtil;
import br.com.mouseweb.security.UserSS;
import br.com.mouseweb.services.AuthService;
import br.com.mouseweb.services.UserService;

// Class (Edpoint) -> Refresh token 

@RestController
// AuthResource com caminho /auth 
@RequestMapping(value = "/auth")
public class AuthResource {
	
	// Nota: por que usamos POST e não GET, PUT ou DELETE? 
	//      - GET, PUT e DELETE devem ser usados para operações IDEMPOTENTES. 
	//      Uma operação idempotente é aquela que, se usada mais de uma vez, 
	//      produz o mesmo resultado se usada uma única vez
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired 
	private AuthService service;

	// EdPoint protegido por autenticação o usuario tem que está logado para acessa o -> (/refresh_token) 
	//   se não vai da um (Forbidden).
	// Passou no filtro de autorização ele cai nesse método.
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		// Pego o usuário logado 
		UserSS user = UserService.authenticated();
		// Gera um novo (Token), com o usuário -> vai ser gerado um Token com a data atual o tempo de 
		//   expiração será renovado.
		String token = jwtUtil.generateToken(user.getUsername());
		// Adiciona o (Token) na resposta da requisição.
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization"); 
		return ResponseEntity.noContent().build();
	}
	
	// Edpoint = Esqueci a senha 
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	// Recebi como parametro o e-mail que a pessoa informa lá, como é um (POST) dei preferencia para receber esse e-mail
	//   na forma de um request body - criando um DTO expecifico para isso -> EmailDTO.
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) {
		// Chama o método de serviço -> sendNewPassword()
		service.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();
	}
	
}



