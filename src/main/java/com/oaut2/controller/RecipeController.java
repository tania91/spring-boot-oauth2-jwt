package com.oaut2.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oaut2.dto.Check;
import com.oaut2.dto.RecipeModel;
import com.oaut2.entity.RecipeEntity;
import com.oaut2.service.RecipeService;
import com.oaut2.repository.RecipeRepository;

@Controller
@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecipeController {
	
	 @Autowired
	 private RecipeService recipeService;
	 
	 @Autowired
	 private RecipeRepository recipeRepository;
	 
	 // Devolver todas las recetas 
	 @RequestMapping(value="/all", method = RequestMethod.GET)
	 public List<RecipeEntity> listRecipe(){
        return recipeService.findAll();
	 }
	// Devolver todas las recetas con token
	 @RequestMapping(value="/recipes", method = RequestMethod.GET)
	 public List<RecipeEntity> listRecipeWithToken(){
        return recipeService.findAll();
	 }
	 
	 //Devolver todas lasa recetas de un usuario
	 @RequestMapping(value="/user/{flag}", method = RequestMethod.GET)
	 public List<RecipeEntity> listRecipeUser(@PathVariable(value = "flag") String flag, HttpServletRequest request, SecurityContextHolderAwareRequestWrapper requestWrapper) throws Exception{
		 int id = 0;
		
		 checkIdentity();

		 HttpSession session = request.getSession();
	   	 id = (int) session.getAttribute("id");
	   	 if(flag.equals("tercero")){
			 id = id + 1000; 
		 }
		 return recipeService.findById(id); 
		
	 }
	 
	// Guerdar receta 
	// @PreAuthorize("hasRole('ADMIN')")
	 @RequestMapping(value="/save", method = RequestMethod.POST)
	 public Check save(@RequestBody RecipeModel recipe, HttpServletRequest request) throws Exception{
		 
		 checkIdentity();
		 
		 RecipeEntity entity = new RecipeEntity();
		 entity.setDescripcion(recipe.getDescripcion());
		 entity.setImagen(recipe.getImagen());
		 entity.setIngredientes(recipe.getIngredientes());
		 entity.setNombreReceta(recipe.getNombreReceta());
		 
		 HttpSession session = request.getSession();
	   	 int id = (int) session.getAttribute("id");
	   	 
	   	 if(recipe.getFlag().equals("tercero")){
	   		id = id + 1000;
	   	 }
	   	 entity.setIdentificadorUsuario(id);
		 RecipeEntity entidad = recipeRepository.save(entity);
		 Check check = new Check();
		 if(entidad != null) {
			 check.setConeccion("succes");
		 }else {
			 check.setConeccion("");
		 }
        return check;
	 }
	 
	 private void checkIdentity() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		    
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (!"USER".equals(auth.getAuthority())) {
            	throw new AccessDeniedException("Acceso denegado");
            }	
        }
	 }
}
