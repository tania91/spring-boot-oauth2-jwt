package com.oaut2.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.oaut2.entity.ClientEntity;

@Component
public class ClientService {

	@PersistenceContext
    private EntityManager manager;
	
	public List<ClientEntity> find(String client_id, String client_secret ) throws Exception {
		List<ClientEntity> tokens = new ArrayList<ClientEntity>(0);
		try {
			
			String queryString  = "SELECT * FROM client WHERE CLIENT = 'cocinarusa.es' AND CLIENT_ID ='"+ client_id +"' AND CLIENT_SECRET = '"+ client_secret +"'";
			
			tokens = manager.createNativeQuery(queryString,  ClientEntity.class)
					.getResultList();
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		};
		return tokens;
	}
}
