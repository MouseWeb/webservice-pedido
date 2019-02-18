package br.com.mouseweb.resources.exception;

import java.io.Serializable;

public class FieldMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Nome do campo de erro
	private String fieldName;
	
	// Mensagem de erro
	private String message;
	
	public FieldMessage() {
		
	}

	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
