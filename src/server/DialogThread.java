package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import common.Contact;
import common.Message;

public class DialogThread extends Thread {
	Socket socket;
	int id;
	ArrayList<DialogThread> listeClient;
	ArrayList<DialogThread> listeClientsAccepte;
	String nomClient;
	//PrintWriter out = null;
	//BufferedReader in = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	public DialogThread(Socket s, int id, ArrayList<DialogThread> listeClient) {
		this.socket = s;
		this.id = id;
		this.listeClient = listeClient;
	}

	public void run() {
		boolean running = true;
		try {
			//out = new PrintWriter(socket.getOutputStream(), true);
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in  = new ObjectInputStream(socket.getInputStream());
			
			/*nomClient = (String) in.readObject();
			diffuserMsg(nomClient+" vient de se connecter!");
			*/
			while (running) {
				Message message = (Message) in.readObject();
				//String msg = message.getMessage();
				
				switch(message.getType()) {
					case Message.MESSAGE:
						String msg = message.getMessage();
						diffuserClientsAccepte(new Message(Message.MESSAGE, nomClient + " dit: " + msg));
						System.out.println("      "+nomClient + " dit: " + msg);
					break;
					
					case Message.CONNEXION:
						nomClient = message.getMessage();
						diffuserMsg(new Message(Message.MESSAGE, nomClient + " vient de se connecter!"));
						System.out.println(nomClient + " c'est connecté.");
						
						listeClientsAccepte = new ArrayList<DialogThread>(listeClient);

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