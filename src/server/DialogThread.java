package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;

import common.Contact;
import common.Message;
import common.RSA;
import common.publicKey;

public class DialogThread extends Thread {
	Socket socket;
	int id;
	ArrayList<DialogThread> listeClient;
	ArrayList<DialogThread> listeClientsAccepte;
	String nomClient;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	RSA rsa;
	publicKey clientPublicKey;

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
					case Message.PUBLIC_KEY: // Réception de la clé public du client
						clientPublicKey = message.getPublicKey();
						System.out.println("Réception de la clé public du client " + nomClient + " : ");
						System.out.println("e : " + clientPublicKey.getE());
						System.out.println("n : " + clientPublicKey.getN());
					break;
				
					case Message.MESSAGE:
						String msg = message.getMessage();
						diffuserClientsAccepte(new Message(Message.MESSAGE, nomClient + " dit: " + msg));
						System.out.println("      "+nomClient + " dit: " + msg);
					break;
					
					case Message.CRYPTED_MESSAGE:
						System.out.println("Réception d'un message crypté :");
						String[] crypted_msg = message.getMsg();
						String decrypted_msg = rsa.decrypt(crypted_msg);
						for(int i = 0; i < crypted_msg.length; i++)
						{
							System.out.println(crypted_msg[i]);
						}
						System.out.println("Décryption du message :");
						System.out.println(decrypted_msg);
						diffuserCryptedMessage(decrypted_msg);
						//diffuserClientsAccepte(new Message(Message.MESSAGE, nomClient + " dit (uncrypted) : " + decrypted_msg));
					break;
					
					case Message.CONNEXION:
						// Réception du nom du client
						nomClient = message.getMessage();
						diffuserMsg(new Message(Message.MESSAGE, nomClient + " vient de se connecter!"));
						System.out.println(nomClient + " c'est connecté.");
						
						// On initialise la liste des clients accepté avec la liste des clients
						listeClientsAccepte = new ArrayList<DialogThread>(listeClient);

						// On créé une liste de contact utilisé par le client pour mettre à jour sa vue (cases à cocher) 
						ArrayList<Contact> liste = new ArrayList<Contact>();

						for(int i = 0; i < listeClient.size(); ++i) {
							DialogThread clientThread = listeClient.get(i);
							//if(clientThread.id != this.id)
								liste.add(new Contact(clientThread.id, clientThread.nomClient));
						}
						
						diffuserMsg(new Message(Message.LISTES_CLIENTS, liste)); // Envoi de la liste des clients à chaque client
						for(int i = 0; i < listeClient.size(); ++i) { 	// active l'envoi de messages vers ce client, par tous les autres
							DialogThread clientThread = listeClient.get(i);
							if (clientThread.id != this.id)
								clientThread.listeClientsAccepte.add(this);
						}
						
						ecrireMsg(new Message(Message.PUBLIC_KEY, rsa.getPublic_key()));
					break;
					
					case Message.DECONNEXION:
						running = false;
						remove(id);
						close();
						
						//
						ArrayList<Contact> liste2 = new ArrayList<Contact>();
						for(int i = 0; i < listeClient.size(); ++i) {
							DialogThread clientThread = listeClient.get(i);
								liste2.add(new Contact(clientThread.id, clientThread.nomClient));
						}
						diffuserMsg(new Message(Message.LISTES_CLIENTS, liste2)); // Envoi de la liste des clients à chaque client
						//
						
						diffuserMsg(new Message(Message.MESSAGE, nomClient + " c'est déconnecter!"));
						System.out.println(nomClient + " c'est déconnecté.");
					break;
					
					case Message.LISTES_CLIENTS:
						ecrireMsg(new Message(Message.LISTES_CLIENTS, "liste clients"));
					break;
					
					case Message.DESACTIVER_CLIENT:
						int idClient = message.getId();
						for(int i = 0; i < listeClientsAccepte.size(); ++i) {
							DialogThread clientThread = listeClientsAccepte.get(i);
							if(clientThread.id == idClient) {
								System.out.println(nomClient + " a désactivé "+clientThread.nomClient);
								listeClientsAccepte.remove(i);
							}
						}
					break;
					
					case Message.ACTIVER_CLIENT:
						int idClientActive = message.getId();
						System.out.println("activation du clien"+idClientActive);
						for(int i = 0; i < listeClient.size(); ++i) {
							DialogThread clientThread = listeClient.get(i);
							if(clientThread.id == idClientActive) {
								System.out.println(nomClient + " a activé "+clientThread.nomClient);
								listeClientsAccepte.add(clientThread);
							}
						}
					break;
				}
			}
			
			//remove(id);
			close();

		}
		catch(Exception e) {
			System.err.println(e);
		}
	}

	public void ecrireMsg(Message msg) {
		try {
			out.writeObject(msg);
			out.flush();
		}
		catch(Exception e) {
			System.out.println("Exception envoie message vers le client: " + e);
		}
	}
	
	private synchronized void diffuserMsg(Message msg) {
		for(int i = listeClient.size(); --i >= 0;) {
			DialogThread clientThread = listeClient.get(i);
			clientThread.ecrireMsg(msg);
		}
	}
	
	private synchronized void diffuserClientsAccepte(Message msg) {
		for(int i = listeClientsAccepte.size(); --i >= 0;) {
			DialogThread clientThread = listeClientsAccepte.get(i);
			clientThread.ecrireMsg(msg);
		}
	}
	
	private synchronized void diffuserCryptedMessage(String msg) {
		for(int i = listeClientsAccepte.size(); --i >= 0;) {
			DialogThread clientThread = listeClientsAccepte.get(i);
			//Message mess = new Message(Message.CRYPTED_MESSAGE, nomClient + " dit (crypted) : " + rsa.encrypt(msg, clientThread.clientPublicKey));
			Message mess = new Message(Message.CRYPTED_MESSAGE, rsa.encrypt(msg, clientThread.clientPublicKey));
			clientThread.ecrireMsg(mess);
		}
	}
	
	synchronized void remove(int id) {
		for(int i = 0; i < listeClient.size(); ++i) {
			DialogThread clientThread = listeClient.get(i);
			if(clientThread.id == id)
				listeClient.remove(i);
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
}