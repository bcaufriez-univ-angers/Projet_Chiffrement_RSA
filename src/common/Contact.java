package common;

import java.io.Serializable;

public class Contact implements Serializable {

	protected static final long serialVersionUID = 1112122200L;
	
	int id;
	String nom;
	
	public Contact(int id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}
}