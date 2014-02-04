package common;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	private static final long serialVersionUID = -3931591414600840839L;
	
	public static final int LISTES_CLIENTS = 0;
	public static final int MESSAGE = 1;
	public static final int DECONNEXION = 2;
	public static final int CONNEXION = 3;
	public static final int ACTIVER_CLIENT = 4;
	public static final int DESACTIVER_CLIENT = 5;
	public static final int NOUVEAU_CLIENT = 6;
	public static final int PUBLIC_KEY = 7;
	public static final int CRYPTED_MESSAGE = 8;
	int type;
	String message;
	String[] msg;
	ArrayList<Contact> liste;
	int id;
	publicKey publicKey;
	
	public Message(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public Message(int type, String[] msg) {
		this.type = type;
		this.msg = msg;
	}
	
	public Message(int type, String message,  publicKey publicKey) {
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

	public Message(int type, publicKey publicKey) {
		this.type = type;
		this.publicKey = publicKey;
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

	public publicKey getPublicKey() {
		return publicKey;
	}

	public String[] getMsg() {
		return msg;
	}
	
}
