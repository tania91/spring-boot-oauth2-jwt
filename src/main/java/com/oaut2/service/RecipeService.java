package com.oaut2.service;

import com.oaut2.entity.RecipeEntity;
import com.oaut2.repository.RecipeRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class RecipeService  {
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@PersistenceContext
    private EntityManager manager;
	
	//Devuelve todas las recetas de todos usuarios
	public List<RecipeEntity> findAll() {
		List<RecipeEntity> list = new ArrayList<>();
		recipeRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
	
	//Devuelve todas las recetas de un usuario
	
	public List<RecipeEntity> findById(int id) throws Exception {
		List<RecipeEntity> list = new ArrayList<>();
		
		try {
			
			String queryString  = "SELECT r.* FROM recetas r WHERE r.ID_USUARIO =" + id;
			
			list = manager.createNativeQuery(queryString,  RecipeEntity.class)
                    .getResultList();
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		};
		
		return list;
		
	}
	
	

	public List<RecipeEntity> save(RecipeEntity user) throws Exception {
		List<RecipeEntity> receta = new ArrayList<RecipeEntity>(0);
		try {
			
			String queryString  = "INSERT into recetas (descripcion, id_usuario, imagen, ingredientes, nombre) values ("+user.getDescripcion()+","+ user.getIdentificadorUsuario()+","+user.getImagen()+","+user.getIngredientes()+ ","+user.getNombreReceta()+")";
			
			receta = manager.createNativeQuery(queryString,  RecipeEntity.class)
					.getResultList();
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		};
		return null;
	}
	
	@Transactional
	public int delete(int id) throws Exception {
		int deletedCount = 0;
		try {
			
			String queryString  = "DELETE FROM recetas WHERE ID_USUARIO = " + id;
			
			deletedCount = manager.createNativeQuery(queryString,  RecipeEntity.class).executeUpdate();
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		};
		return deletedCount;
	}
	
}
