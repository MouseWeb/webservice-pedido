package br.com.mouseweb.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	// PALAVRA QUE SERÁ ENBARALHADA JUNTO COM TOKEN
	@Value("${jwt.secret}")
	private String secret;
	
	// TEMPO DE EXPIRAÇÃO
	@Value("${jwt.expiration}")
	private Long expiration;
	
	// GERA UM (TOKEN)
	public String generateToken(String username) {
		return Jwts.builder()
				// USUARIO 
				.setSubject(username)
				// TEMPO DE EXPIRAÇÃO, O TEMPO ATUAL BASEADO NO TEMPO DE EXPIRAÇÃO.
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// DIZ COMO SERÁ ASSINADO O TOKEN -> O ALGORITIMO DE ASSINATURA + O SEGREDO.
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	// VERIFICA SE O TOKEN É VALIDADO
	public boolean tokenValido(String token) {
		// Claims = é um tipo do JWT que ele armazena as reindivicações do Token
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}
	
	// Pega o usuario apartir do Token
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
	
	// Obteim o Claims apartir de um Token
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}
		catch (Exception e) {
			return null;
		}
	}
	
}