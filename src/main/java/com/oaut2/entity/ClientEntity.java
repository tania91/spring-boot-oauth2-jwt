package com.oaut2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Entity
@Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
public class ClientEntity {
	

		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO )
		@Column(name ="id", nullable = false)
		private  Integer id;
		
		@Column(name ="CLIENT", length=100)
		private String acces_toekn;
		
		@Column(name ="CLIENT_ID", length=100)
		private String client_id;
		
		@Column(name ="CLIENT_SECRET", length=200)
		private String client_secret;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getAcces_toekn() {
			return acces_toekn;
		}

		public void setAcces_toekn(String acces_toekn) {
			this.acces_toekn = acces_toekn;
		}

		public String getClient_id() {
			return client_id;
		}

		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}

		public String getClient_secret() {
			return client_secret;
		}

		public void setClient_secret(String client_secret) {
			this.client_secret = client_secret;
		}
		
		

}
