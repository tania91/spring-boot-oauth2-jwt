package com.oaut2.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecipeModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3762094288839689020L;
	
	private int identificadorUsuario;
	private String nombreReceta;
	private String descripcion;
	private String imagen;
	private String ingredientes;
	private String flag;
}
