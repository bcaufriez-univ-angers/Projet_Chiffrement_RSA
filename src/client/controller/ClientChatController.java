package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import client.model.ClientChat;
import client.model.Login;
import client.view.ClientChatWindow;
import client.view.LoginWindow;

import common.Contact;
import common.Message;

public class ClientChatController implements ActionListener, KeyListener/*, ItemListener */{
	
	ClientChat model;
	ClientChatWindow view;
	ListeningClientThread listeningClientThread;
	
	public ClientChatController(ClientChat model, ClientChatWindow view) {
		System.out.println("Constructor Client Chat Controller");
		this.model = model;
		this.view = view;
	}
	
	public void openSocket() {
        listeningClientThread = new ListeningClientThread(this, model);
        listeningClientThread.start();
        System.out.println("ListeningThread created!");
    }
	
	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("Press ENTER key");
			if ( !view.getSaisie().getText().equals("") ) { // Si la zone de saisie n'est pas vide
				//sendMessage(new Message(Message.MESSAGE, view.getSaisie().getText()));
				sendMessage(new Message(Message.CRYPTED_MESSAGE, model.encrypt(view.getSaisie().getText())));
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == view.quitter){
			sendMessage(new Message(Message.DECONNEXION, ""));
		}
		
		if (source == view.envoyer){
			if ( !view.getSaisie().getText().equals("") ) { // Si la zone de saisie n'est pas vide
				//sendMessage(new Message(Message.MESSAGE, view.getSaisie().getText()));
				sendMessage(new Message(Message.CRYPTED_MESSAGE, model.encrypt(view.getSaisie().getText())));
			}
		}
	}
	
	public void sendMessage(Message message) {
		switch(message.getType()) {
			case Message.MESSAGE:
				model.sendMessage(message);
				view.getSaisie().setText(""); // On efface la zone de saisie
			break;
			
			case Message.CRYPTED_MESSAGE:
				model.sendMessage(message);
				view.getSaisie().setText(""); // On efface la zone de saisie
			break;
			
			case Message.DECONNEXION:
				model.sendMessage(message);
				listeningClientThread.stop(); // On stop le thread d'écoute
				model.stop();
				System.exit(0);
			break;
		} 
	}
	
	public void receivedMessage(Message message) {
		switch(message.getType()) {
			case Message.MESSAGE:
				String msg = message.getMessage();
				view.getAffichage().append(msg+"\n"); // On affiche le message dans la zone d'affichage
				System.out.println(msg);
			break;
			
			case Message.CRYPTED_MESSAGE:
				System.out.println("Début mess cripté");
				String[] mess = message.getMsg();
				String str = model.getRsa().decrypt(mess);
				view.getAffichage().append(str+"\n"); // On affiche le message dans la zone d'affichage
				System.out.println(str);
				System.out.println("Fin mess cripté");
			break;
			
			case Message.PUBLIC_KEY: // Réception de la clé public du serveur
				model.setServerPublicKey(message.getPublicKey());
				System.out.println("Réception de la clé public du server : ");
				System.out.println("e : " + message.getPublicKey().getE());
				System.out.println("n : " + message.getPublicKey().getN());
			break;
			
			case Message.LISTES_CLIENTS:
				model.setListe(message.getListe());
				ArrayList<Contact> liste = new ArrayList<Contact>();
				liste = message.getListe();
				System.out.println("Il y a " + liste.size() + " clients connecté:");
				for(int i = 0; i < liste.size(); ++i) {
					System.out.println("ID : " + liste.get(i).getId() + " - Name : " + liste.get(i).getNom());
				}
				//fenetre.refreshListeClient(liste);
				//view.updateListeClient();
				updateListeClient();
			break;
			
			case Message.NOUVEAU_CLIENT:
				int idClient = message.getId();
				model.sendMessage(new Message(Message.ACTIVER_CLIENT, idClient));
			break;
		} 
	}

	public void updateListeClient() {
		view.getListeClient().removeAll();
		view.getListeClient().setLayout(new BoxLayout(view.getListeClient(), BoxLayout.Y_AXIS));
		
		ArrayList<Contact> liste = model.getListe();
		JCheckBox[] tab = new JCheckBox[liste.size()];
		for(int i=0; i< liste.size(); i++) {
			final int id = liste.get(i).getId();
			tab[i] = new JCheckBox(liste.get(i).getNom());
			tab[i].setSelected(true);
			tab[i].addItemListener(new ItemListener() {
				
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						System.out.println("Id du client désactivé: "+id);
						model.sendMessage(new Message(Message.DESACTIVER_CLIENT, id));
					}
					if (e.getStateChange() == ItemEvent.SELECTED) {
						System.out.println("Id du client activé: "+id);
						model.sendMessage(new Message(Message.ACTIVER_CLIENT, id));
					}
					
				}
			});

			view.getListeClient().add(tab[i]);
		}
		view.getListeClient().revalidate();
		view.getListeClient().repaint();		
	}
}
