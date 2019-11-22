package com.oaut2.controller;

import com.oaut2.dto.Check;
import com.oaut2.dto.User;
import com.oaut2.entity.UserEntity;
import com.oaut2.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;
    
    /************
     * Añadir usuario
     * @param user
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public User create(@RequestBody UserEntity userModel){
    	userModel.setRole("USER");
    	userModel.setEnabled((short) 1);
    	userModel.setId_client("nhfgbdt.F4KJUasnzpriWPKMVNHF98");
    	
    	UserEntity entity = userService.save(userModel);
    	User user = new User();
    	
    	user.setUsername(entity.getUsername());
    	user.setRole(entity.getRole());
    	user.setId(entity.getId());
    	
    	return user;
    	
    	
    }
    /************
     * Buscar el username
     * @param userName
     * @return
     */
    @RequestMapping(value = "/findByName", method = RequestMethod.POST)
    public Check create(@RequestBody User  userName){
    	Check check = new Check();
    	
    	UserEntity entity = userService.findByName(userName.getUsername());
    	if(entity != null) {
    		check.setConeccion("KO");
    	}else {
    		check.setConeccion("OK");
    	}
        return check;
    }
    

//    @RequestMapping(value = "/user", method = RequestMethod.GET)
//    public User findById(HttpServletRequest request) throws Exception{
//    	
//    	checkIdentity();
//    	
//    	HttpSession session = request.getSession();
//   	  	int id = (int) session.getAttribute("id");
//   	  	
//   	  	List<UserEntity> entidad =  userService.findById(id);
//   	  	User user = new User();
//   	  	
//   	  	user.setId(entidad.get(0).getId());
//   	  	user.setRole(entidad.get(0).getRole());
//   	  	user.setUsername(entidad.get(0).getUsername());
//   	  	
//   	  	
//   	  	
//   	  	return user;
//    }
    
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
