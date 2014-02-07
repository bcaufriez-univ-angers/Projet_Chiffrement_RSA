package client.model;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Color;
import java.io.*;

import client.controller.ListeningClientThread;
import common.Client;
import common.Message;
import common.RSA;
import common.publicKey;
 
public class ClientChat {
	private static boolean displayLog = true;
	
	private String name;
	private String server;
	private int port;
	private int keySize;
	
	//boolean running;
	private Socket socket;
	private ObjectInputStream in = null;
	ObjectOutputStream out = null;
	ListeningClientThread listeningClientThread;
	public ArrayList<Client> liste;
	publicKey serverPublicKey;
	private Color textColor;
	boolean encrypt;
	RSA rsa;
	Boolean run;

	public ClientChat(String name, String server, int port, int keySize) {
		this.name = name;
		this.server = server;
		this.port = port;
		this.keySize = keySize;
		this.encrypt = true;

		// Connection au serveur
		try {
			socket = new Socket(server, port);
		} 
		catch(Exception e) {
			System.out.println("Unable to connect to server");
			stop();
		}
		
		// Création des Input/output Streams
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e) {
			System.out.println("Exception create new Input/output Streams");
			stop();
		}
		rsa = new RSA(keySize);

		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		textColor = new Color(R, G, B); //random color
	}
	
	public void start(){
		this.rsa.generateKeys();
		log("Création des clés privé/public");
		
		try {
			// Envoi le nom et la clé public du client au serveur
			try
			{
				Client client = new Client(name, rsa.getPublic_key(), textColor, Client.COMPUTER);
				sendMessage(new Message(Message.CONNECTION, client));
				log("Envoi demande de connexion au server");
				log("Envoi de la clé publique au serveur");
			}
			catch (Exception e) {
				System.out.println("Exception doing login : " + e);
			}
			
			while (run) {
				Scanner sc = new Scanner(System.in);
				String msg = sc.nextLine();
				sendMessage(new Message(Message.MESSAGE, msg));
			}

		}
		catch(Exception e) {
			System.err.println("Client: "+e);
		}
	}
	
	// Fermeture de la connexion
	public void stop() {
		//running = false;
		try {
			if(out != null) out.close();
		}
		catch(Exception e) {}
		try {
			if(in != null) in.close();
		}
		catch(Exception e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (Exception e) {}
	}
	
	public String[] encrypt(String str) {
        return this.rsa.encrypt(str, serverPublicKey);
	}
	
	public void sendMessage(Message msg) {
		try {
			out.reset();
			out.writeObject(msg);
			out.flush();
		}
		catch(Exception e) {
			System.out.println("Exception envoie message vers le serveur: " + e);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	public ArrayList<Client> getListe() {
		return liste;
	}

	public void setListe(ArrayList<Client> liste) {
		this.liste = liste;
	}

	public publicKey getServerPublicKey() {
		return serverPublicKey;
	}

	public void setServerPublicKey(publicKey serverPublicKey) {
		this.serverPublicKey = serverPublicKey;
	}

	public RSA getRsa() {
		return rsa;
	}
	
	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public boolean isEncrypt() {
		return encrypt;
	}
	
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
	
	private void log(String log) {
		if (displayLog) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("["+ dateFormat.format(date) + "] - " +log);
		}
	}
}