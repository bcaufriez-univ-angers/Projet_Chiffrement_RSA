package common;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	public static final int LISTES_CLIENTS = 0;
	public static final int MESSAGE = 1;
	public static final int DECONNEXION = 2;
	public static final int CONNEXION = 3;
	public static final int ACTIVER_CLIENT = 4;
	public static final int DESACTIVER_CLIENT = 5;
	public static final int NOUVEAU_CLIENT = 6;
	int type;
	String message;
	ArrayList<Contact> liste;
	int id;
	
	public Message(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public Message(int type, ArrayList<Contact> liste) {
		this.type = type;
		this.liste = liste;
	}
	
	public Message(int type, int id) {
		this.type = type;
		this.id = id;
	}

	public int getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public ArrayList<Contact> getListe() {
		return liste;
	}
	
	public int getId() {
		return id;
	}
}
