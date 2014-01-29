package client.model;
import java.net.*;
import java.util.*;
import java.io.*;

import client.view.ClientChatWindow;
import common.Contact;
import common.Message;
 
public class ClientChat {
	//private PrintWriter out = null;
	//private BufferedReader in = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	private Socket socket;
	boolean running;
	private String addServeur;
	private int numPort;
	private String nomClient;
	ClientChatWindow fenetre;
	
	public ClientChat(String addServeur, int numPort, String nomClient/*, FenetreClient2 fenetre*/) {
		this.addServeur = addServeur;
		this.numPort = numPort;
		this.nomClient = nomClient;
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

	/*
	public void start(){
		System.out.println("début start !");
		System.out.println(addServeur + " " + numPort + " " + nomClient);
		running = true;
		try {
			// Connection au serveur
			try {
				socket = new Socket(addServeur, numPort);
			} 
			catch(Exception e) {
				System.out.println("Erreur de connection au serveur");
				close();
				fenetre.quitter();
			}			
			
			// Création des Input/output Streams
			try
			{
				out = new ObjectOutputStream(socket.getOutputStream());
				in  = new ObjectInputStream(socket.getInputStream());
			}
			catch (IOException e) {
				System.out.println("Exception creation new Input/output Streams");
				close();
				fenetre.quitter();
			}
			
			
			// Lancement du Thread qui écoute le serveur
			new ListenFromServer().start();
			
			// Envoi le nom du client au serveur
			try
			{
				sendMessage(new Message(Message.CONNEXION, nomClient));
			}
			catch (Exception e) {
				System.out.println("Exception doing login : " + e);
			}
			
			//out.writeObject(new Message(Message.CONNEXION, nomClient));
		
			while (running) {
				Scanner sc = new Scanner(System.in);
				String msg = sc.nextLine();
				sendMessage(new Message(Message.MESSAGE, msg));
			}

			close();
		}
		catch(Exception e) {
			System.err.println("Client: "+e);
		}
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
	
	// Fermeture de la connexion
	public void close() {
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
	*/
	//public static void main(String args[]) {
		//Connexion jd = new Connexion();
		//jd.setVisible(true);	
		/*FenetreClient2 fen = new FenetreClient2();
		Client client = new Client("localhost", 30970, "Benoit", fen);
		fen.connectToClientProcess(client);
		client.start();
		*/
	//}
	/*
	public void setFenetre(ClientChatWindow fen) {
		this.fenetre = fen;
	}
	
	class ListenFromServer extends Thread {
		public void run() {
			while (running) {
				try {
					Message message = (Message) in.readObject();
					
					switch(message.getType()) {
						case Message.MESSAGE:
							String msg = message.getMessage();
							fenetre.setAffichage(msg);
							System.out.println("lala");
							System.out.println(msg);
						break;
						
						case Message.LISTES_CLIENTS:
							ArrayList<Contact> liste = new ArrayList<Contact>();
							liste = message.getListe();
							System.out.println("Il y a " + liste.size() + " clients connecté:");
							for(int i = 0; i < liste.size(); ++i) {
								System.out.println(liste.get(i).getNom());
							}
							fenetre.refreshListeClient(liste);
						break;
						
						case Message.NOUVEAU_CLIENT:
							int idClient = message.getId();
							sendMessage(new Message(Message.ACTIVER_CLIENT, idClient));
						break;
					} 
				}
				catch(Exception e) {
					System.out.println("Exception ecoute du serveur: " + e);
				}
			}
		}
	}
	*/
}