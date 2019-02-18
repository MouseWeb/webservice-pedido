package br.com.mouseweb.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// FILTRO QUE ANALISA O TOKEN PRA VER SE É VALIDO
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	// METHODO QUE INTERCEPTA A REQUISIÇÃO E VER SE É VALIDO O TOKEN
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
		
		// PEGA O VALOR DO CABECARIO -> Authorization = Token
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}
	
	// VAI RETORNA NULL SE O TOKEN FOR INVALIDO
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			// BUSCA NO BANCO DE DADOS O USER
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}