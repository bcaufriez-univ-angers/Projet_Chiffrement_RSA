package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	private static final long serialVersionUID = -3931591414600840839L;
	
	public static final int CONNECTION = 1;
	public static final int DECONNECTION = 2;
	public static final int MESSAGE = 3;
	public static final int CRYPTED_MESSAGE = 4;
	public static final int NOTIFICATION = 5;
	public static final int CHANGE_TEXT_COLOR = 6;
	public static final int NEW_CLIENT = 7;
	public static final int ENABLE_CLIENT = 8;
	public static final int DISABLE_CLIENT = 9;
	public static final int LIST_CLIENTS = 10;

	int type;
	String message;
	String[] msg;
	ArrayList<Client> liste;
	int id;
	publicKey publicKey;
	Client client;
	MyColor color;
	
	public Message(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public Message(int type, Client client) {
		this.type = type;
		this.client = client;
	}
	
	public Message(int type, MyColor color) {
		this.type = type;
		this.color = color;
	}
	
	public Message(int type, String message,  MyColor color) {
		this.type = type;
		this.message = message;
		this.color = color;
	}
	
	public Message(int type, String[] msg,  MyColor color) {
		this.type = type;
		this.msg = msg;
		this.color = color;
	}
	
	public Message(int type, String[] msg) {
		this.type = type;
		this.msg = msg;
	}
	
	public Message(int type, String message,  publicKey publicKey) {
		this.type = type;
		this.message = message;
	}
	
	public Message(int type, ArrayList<Client> liste) {
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

	public ArrayList<Client> getListe() {
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

	public Client getClient() {
		return client;
	}

	public MyColor getColor() {
		return color;
	}

	public void setColor(MyColor color) {
		this.color = color;
	}
}
