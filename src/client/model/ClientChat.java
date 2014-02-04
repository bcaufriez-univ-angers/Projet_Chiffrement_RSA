package client.model;
import java.net.*;
import java.util.*;
import java.io.*;

import client.controller.ListeningClientThread;
import client.view.ClientChatWindow;
import common.Contact;
import common.Message;
import common.RSA;
import common.publicKey;
 
public class ClientChat {
	boolean running;
	private Socket socket;
	private ObjectInputStream in = null;
	ObjectOutputStream out = null;
	private String addServeur;
	private int numPort;
	private String nomClient;
	ListeningClientThread listeningClientThread;
	public ArrayList<Contact> liste;
	publicKey serverPublicKey;
	RSA rsa = new RSA();

	public ClientChat(String addServeur, int numPort, String nomClient) {
		this.addServeur = addServeur;
		this.numPort = numPort;
		this.nomClient = nomClient;

		// Connection au serveur
		try {
			socket = new Socket(addServeur, numPort);
		} 
		catch(Exception e) {
			System.out.println("Impossible de se connecter au serveur");
			stop();
		}
		
		// Création des Input/output Streams
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e) {
			System.out.println("Exception creation new Input/output Streams");
			stop();
		}
	}
	
	public void start(){
		System.out.println("Lancement du ClientChat : " + nomClient);
		System.out.println(addServeur + " " + numPort + " " + nomClient);
		System.out.println("Création des clés privé/public");
		this.rsa.generateKeys();
		System.out.println("e : " + rsa.getPublic_key().getE());
		System.out.println("n : " + rsa.getPublic_key().getN());
		
		try {
			// Envoi le nom et la clé public du client au serveur
			try
			{
				sendMessage(new Message(Message.CONNEXION, nomClient));
				sendMessage(new Message(Message.PUBLIC_KEY, rsa.getPublic_key()));
			}
			catch (Exception e) {
				System.out.println("Exception doing login : " + e);
			}
		
			while (running) {
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
		running = false;
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
			out.writeObject(msg);
			out.flush();
		}
		catch(Exception e) {
			System.out.println("Exception envoie message vers le serveur: " + e);
		}
	}
	
	public String getAddServeur() {
		return addServeur;
	}

	public int getNumPort() {
		return numPort;
	}

	public String getNomClient() {
		return nomClient;
	}
	
	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	public ArrayList<Contact> getListe() {
		return liste;
	}

	public void setListe(ArrayList<Contact> liste) {
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
	
}