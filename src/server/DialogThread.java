package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.Client;
import common.Message;
import common.RSA;

public class DialogThread extends Thread {
	private static boolean displayLog = true;
	public static final int AVAILABLE_CLIENTS = 0;	// Tous les clients connectés
	public static final int ENABLE_CLIENTS = 1;		// Les clients acceptés
	
	Socket socket;
	int id;
	ArrayList<DialogThread> listeClient;
	ArrayList<DialogThread> listeClientsAccepte;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	RSA rsa;
	Client client;

	public DialogThread(Socket s, int id, ArrayList<DialogThread> listeClient, RSA rsa) {
		this.socket = s;
		this.id = id;
		this.listeClient = listeClient;
		this.rsa = rsa;
	}

	public void run() {
		boolean running = true;
		try {			
			out = new ObjectOutputStream(socket.getOutputStream());
			in  = new ObjectInputStream(socket.getInputStream());
			
			while (running) {
				Message message = (Message) in.readObject();
				
				switch(message.getType()) {

					case Message.MESSAGE:
						String msg = message.getMessage();
						/*
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
						Date date = new Date();
						diffuserMsg(AVAILABLE_CLIENTS, new Message(Message.MESSAGE, "[" + dateFormat.format(date) + "] " + client.getName() + " dit: " + msg));
						*/
						diffuserMsg(AVAILABLE_CLIENTS, new Message(Message.MESSAGE, client.getName() + " dit: " + msg));
						log("Envoi d'un message non crypté par le client \"" + client.getName() + "\"\n" +
								"\t\t\t    " + client.getName() + " dit: " + msg);
					break;
					
					case Message.CRYPTED_MESSAGE:
						String[] crypted_msg = message.getMsg();
						/*
						for(int i = 0; i < crypted_msg.length; i++)
							System.out.println(crypted_msg[i]);
						*/
						String decrypted_msg = rsa.decrypt(crypted_msg);
						diffuserMsg(ENABLE_CLIENTS, new Message(Message.CRYPTED_MESSAGE, decrypted_msg));
						log("Envoi d'un message crypté par le client \"" + client.getName() + "\"\n" +
								"\t\t\t    " + client.getName() + " dit: " + decrypted_msg);
					break;
					
					case Message.CONNECTION:
						// Réception des informations du client
						client = message.getClient();
						client.setId(id); // On attribue l'identifiant au client
						if (client.getDeviceType() == Client.COMPUTER)
							log("Réception demande de connexion au server du client \"" + client.getName() + "\" à partir d'un \"ordinateur\"");
						if (client.getDeviceType() == Client.MOBILE)
							log("Réception demande de connexion au server du client \"" + client.getName() + "\" à partir d'un \"mobile\"");
						
						log("Réception de la clé publique du client \"" + client.getName() + "\"" + "\n" +
								"\t\t\t    " + "e : " + client.getPublicKey().getE() + "\n" +
								"\t\t\t    " + "n : " + client.getPublicKey().getN());
						
						// Le serveur envoie sa clé publique au client
						ecrireMsg(new Message(Message.CONNECTION, rsa.getPublic_key()));
						log("Envoi de la clé publique du serveur au client \"" + client.getName() + "\"");
						
						// Le serveur notifie aux clients la connexion d'un nouveau client
						diffuserMsg(AVAILABLE_CLIENTS, new Message(Message.NOTIFICATION, client.getName() + " vient de se connecter !"));
						log("Le client \"" + client.getName() + "\" est connecté");

						// Le serveur envoie la liste des clients connectés au client
						sendListClient();

						// On initialise la liste des clients accepté avec la liste des clients
						listeClientsAccepte = new ArrayList<DialogThread>(listeClient);

						for(int i = 0; i < listeClient.size(); ++i) { 	// active l'envoi de messages vers ce client, par tous les autres
							DialogThread clientThread = listeClient.get(i);
							if (clientThread.id != this.id)
								clientThread.listeClientsAccepte.add(this);
						}
						
					break;
					
					case Message.DECONNECTION:
						running = false;
						
						// On supprime le client de la liste
						for(int i = 0; i < listeClient.size(); ++i) {
							DialogThread clientThread = listeClient.get(i);
							if(clientThread.id == id)
								listeClient.remove(i);
						}
						
						// On ferme la connexion du client
						close();
						
						// Le serveur renvoie la liste des clients connectés à chaque client
						sendListClient();
						
						// Le serveur notifie aux clients la déconnexion du client
						diffuserMsg(AVAILABLE_CLIENTS ,new Message(Message.NOTIFICATION, client.getName() + " vient de se déconnecter !"));
						log("Le client \"" + client.getName() + "\" c'est déconnecté.");
					break;
					
					case Message.DISABLE_CLIENT:
						int idClient = message.getId();
						for(int i = 0; i < listeClientsAccepte.size(); ++i) {
							DialogThread clientThread = listeClientsAccepte.get(i);
							if(clientThread.id == idClient) {
								listeClientsAccepte.remove(i);
								log("Le client \"" + client.getName() + " a désactivé le client \"" + clientThread.client.getName() + "\"");
							}
						}
					break;
					
					case Message.ENABLE_CLIENT:
						int idClientActive = message.getId();
						for(int i = 0; i < listeClient.size(); ++i) {
							DialogThread clientThread = listeClient.get(i);
							if(clientThread.id == idClientActive) {
								listeClientsAccepte.add(clientThread);
								log("Le client \"" + client.getName() + " a activé le client \"" + clientThread.client.getName() + "\"");
							}
						}
					break;
					
					case Message.CHANGE_TEXT_COLOR:
						client.setTextColor(message.getColor());
						sendListClient();
						diffuserMsg(AVAILABLE_CLIENTS, new Message(Message.NOTIFICATION, client.getName() + " vient de changer la couleur de son texte !"));
						log("Le client \"" + client.getName() + " vient de changer la couleur de son texte ");
					break;
				}
			}
			close();

		}
		catch(Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * Fonction qui envoie un message au client
	 */
	public void ecrireMsg(Message msg) {
		try {
			out.reset();
			out.writeObject(msg);
			out.flush();
		}
		catch(Exception e) {
			System.out.println("Exception envoie message vers le client: " + e);
		}
	}
	
	/**
	 * Fonction qui envoie à chaque client une liste de tous les clients connecté au chat
	 */
	private void sendListClient() {
		for(int i = listeClient.size(); --i >= 0;) {
			DialogThread currentClient = listeClient.get(i);
			
			ArrayList<Client> liste = new ArrayList<Client>();
			for(int j = 0; j < listeClient.size(); ++j) { // On rempli la liste avec les clients connectés
				DialogThread clientThread = listeClient.get(j);
				if(clientThread.id != currentClient.id)	// On n'ajoute pas le client courant à la liste
					liste.add(clientThread.client);
			}
			currentClient.ecrireMsg(new Message(Message.LIST_CLIENTS, liste));
		}
	}
	
	/**
	 * Fonction qui permet de diffuser un message aux clients du chat
	 */
	private synchronized void diffuserMsg(int authorizedClient, Message msg) {
		if (authorizedClient == AVAILABLE_CLIENTS) { 	// Tous les clients connectés
			for(int i = listeClient.size(); --i >= 0;) {
				DialogThread clientThread = listeClient.get(i);
				clientThread.ecrireMsg(msg);
			}
		}
		else if (authorizedClient == ENABLE_CLIENTS) {	// Les clients acceptés
			for(int i = listeClientsAccepte.size(); --i >= 0;) {
				DialogThread clientThread = listeClientsAccepte.get(i);
				if (msg.getType() == Message.CRYPTED_MESSAGE) {
					String str = msg.getMessage();
					clientThread.ecrireMsg(new Message(Message.CRYPTED_MESSAGE, rsa.encrypt(str, clientThread.client.getPublicKey()), client.getTextColor()));
				}
				else if (msg.getType() == Message.MESSAGE) {
					clientThread.ecrireMsg(msg);
				}
			}
		}
	}
	
	private void close() { // On ferme la connexion
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
	
	private void log(String log) {
		if (displayLog) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("["+ dateFormat.format(date) + "] - " +log);
		}
	}
}