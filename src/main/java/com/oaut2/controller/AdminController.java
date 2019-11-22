package com.oaut2.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oaut2.dto.User;
import com.oaut2.entity.RecipeEntity;
import com.oaut2.entity.UserEntity;
import com.oaut2.repository.UserRepository;
import com.oaut2.service.RecipeService;
import com.oaut2.service.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {
	
	@Autowired
	UserRepository userRepository;
	
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private RecipeService recipeService;

	
	
	 // Devolver todos los usurios 
	 @RequestMapping(value="/all/users", method = RequestMethod.GET)
	 public List<User> listUsers() throws Exception{
		 List<User> users = new ArrayList<User>();
			//Se comprueba el rol del usuario 
			checkIdentity();
			 
	        List<UserEntity> entity = userService.findByRol();
	       
	        entity.stream().forEach(x -> {
	        	User user = new User();
	        	
	        	user.setId(x.getId());
	      	  	user.setRole(x.getRole());
	      	  	user.setUsername(x.getUsername());
	      	  	user.setEmail(x.getEmail());
	      	  	user.setApellidos(x.getApellidos());
	      	  	user.setNombre(x.getNombre());
	      	  	
	      	  	
	      	  	users.add(user);
	        });

	  	  	return users;
	 }
	 
	 @RequestMapping(value="/all/recipe", method = RequestMethod.GET)
	 public List<RecipeEntity> listRecipe() throws Exception{
		 List<RecipeEntity> users = new ArrayList<RecipeEntity>();
		//Se comprueba el rol del usuario 
		checkIdentity();
		 
        List<RecipeEntity> entity = recipeService.findAll();

  	  	return entity;
	 }
	 
	 @RequestMapping(value="/delete/{idCliente}", method = RequestMethod.DELETE)
	 public List<RecipeEntity> deleteUsuario(@PathVariable(value = "idCliente") Integer idCliente) throws Exception{
		 //Se comprueba el rol del usuario 
		 checkIdentity();
		 int delete = recipeService.delete(idCliente);
		 
		 List<UserEntity> entity = new ArrayList<UserEntity>(0);
		 entity = userService.findById(idCliente);
		 
		 userRepository.delete(entity.get(0));

	  	 return null;
	 }
	 
	 @RequestMapping(value="/delete/recipe/{idCliente}", method = RequestMethod.DELETE)
	 public List<RecipeEntity> deleteRecipe(@PathVariable(value = "idCliente") Integer idCliente) throws Exception{
		 //Se comprueba el rol del usuario 
		 checkIdentity();
		 int delete = recipeService.delete(idCliente);
	
	  	 return null;
	 }
	
	 private void checkIdentity() {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			    
	        for (GrantedAuthority auth : authentication.getAuthorities()) {
	            if (!"ADMIN".equals(auth.getAuthority())) {
	            	throw new AccessDeniedException("Acceso denegado");
	            }	
	        }
		 }
}
