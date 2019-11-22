package com.oaut2.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oaut2.dto.Check;



@RestController
@RequestMapping("/conection")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CheckConection {
	
	  @RequestMapping(value = "/", method = RequestMethod.GET)
	    public Check check(){
		  Check conection = new Check();
		  conection.setConeccion("succes");
	      
		  return  conection;
	        
	    }

}
