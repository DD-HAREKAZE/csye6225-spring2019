package io.webApp.springbootstarter.register;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class TableCreation {



	private EntityManager entityManager;
	
	public TableCreation() {
		
	}

	@PersistenceContext
	public boolean createDB() {
		Query query = entityManager.createNativeQuery("Create Database if not exists user_details"
				+ "CREATE TABLE if not exists loginDetails (\n" + 
				"  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n" + 
				"    email VARCHAR(5000) NOT NULL \n" +
				"   password VARCHAR(500) NOT NULL,\n" + 
				"); ");
		return true;
	}

}