package com.oaut2.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "recetas")
@EntityListeners(AuditingEntityListener.class)
public class RecipeEntity {
	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO )
	@Column(name ="ID_RECETA", nullable = false)
	private  int id_receta;
	
	
	@Column(name ="ID_USUARIO")
	private int identificadorUsuario;
	
	@NotBlank
	@Column(name ="NOMBRE")
	private String nombreReceta;
	
	@NotBlank
	@Column(name ="DESCRIPCION", length=1000)
	private String descripcion;
	
	@NotBlank
	@Column(name ="IMAGEN")
	@Lob
	private String imagen;
	
	@NotBlank
	@Column(name ="INGREDIENTES")
	private String ingredientes;
	
	public int getId_receta() {
		return id_receta;
	}

	public void setId_receta(int id_receta) {
		this.id_receta = id_receta;
	}

	public int getIdentificadorUsuario() {
		return identificadorUsuario;
	}

	public void setIdentificadorUsuario(int identificadorUsuario) {
		this.identificadorUsuario = identificadorUsuario;
	}

	public String getNombreReceta() {
		return nombreReceta;
	}

	public void setNombreReceta(String nombreReceta) {
		this.nombreReceta = nombreReceta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}
	
	
}
