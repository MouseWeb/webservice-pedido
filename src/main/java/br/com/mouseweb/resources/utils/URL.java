package br.com.mouseweb.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {
	
	// Metodo para descodificar um parametro, um nome pode vim com espaço.
	public static String decodeParam(String s) { 

		try { 
			
			return URLDecoder.decode(s, "UTF-8"); 
			
		} catch (UnsupportedEncodingException e) { 
			
			return ""; 
		} 

	}	
	
	// Metodo que pega uma String (1,2,3) e converte para uma lista de números Integer.
	public static List<Integer> decodeIntList(String s) { 
		
		// Forma 1 - simples
		/*String[] vet = s.split(","); 
		List<Integer> list = new ArrayList<>(); 
		for (int i=0; i<vet.length; i++) { 
			list.add(Integer.parseInt(vet[i])); 
		} */

		//return list; 
		
		// Forma 2 - Implementado da forma de Lambida (Funcao Lambida).
		return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList()); 
	} 

}
