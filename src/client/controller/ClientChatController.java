 package client.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import client.model.ClientChat;
import client.view.ClientChatWindow;

import common.Client;
import common.Message;
import common.MyColor;

public class ClientChatController implements ActionListener, KeyListener, ItemListener {
	private static boolean displayLog = true;
	
	ClientChat model;
	ClientChatWindow view;
	ListeningClientThread listeningClientThread;
	
	public ClientChatController(ClientChat model, ClientChatWindow view) {
		this.model = model;
		this.view = view;
	}
	
	public void openSocket() {
        listeningClientThread = new ListeningClientThread(this, model);
        listeningClientThread.start();
    }

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if ( !view.getSaisie().getText().equals("") ) { // Si la zone de saisie n'est pas vide
				if (model.isEncrypt() == true)
					sendMessage(new Message(Message.CRYPTED_MESSAGE, model.encrypt(view.getSaisie().getText())));
				else
					sendMessage(new Message(Message.MESSAGE, view.getSaisie().getText()));
			}
		}
	}
	
	/*
	 * Permet de gérer 
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		
		if (source == view.buttonEncrypt){
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				view.buttonEncrypt.setIcon(new ImageIcon(getClass().getResource("/client/icon/buttonNotEncrypt.png")));
				model.setEncrypt(false);
				log("Désactivation du cryptage des messages");
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				view.buttonEncrypt.setIcon(new ImageIcon(getClass().getResource("/client/icon/buttonEncrypt.png")));
				model.setEncrypt(true);
				log("Activation du cryptage des messages");
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == view.quitter){
			sendMessage(new Message(Message.DECONNECTION, ""));
		}
		
		if (source == view.envoyer){
			if ( !view.getSaisie().getText().equals("") ) { // Si la zone de saisie n'est pas vide
				if (model.isEncrypt() == true)
					sendMessage(new Message(Message.CRYPTED_MESSAGE, model.encrypt(view.getSaisie().getText())));
				else
					sendMessage(new Message(Message.MESSAGE, view.getSaisie().getText()));
			}
		}
		
		if (source == view.buttonSendFile){
			JFileChooser choix = new JFileChooser();
			int retour = choix.showOpenDialog(null);
			if(retour == JFileChooser.APPROVE_OPTION){
			   // un fichier a été choisi (sortie par OK)
			   // nom du fichier  choisi 
			   choix.getSelectedFile().getName();
			   // chemin absolu du fichier choisi
			   choix.getSelectedFile().getAbsolutePath();
			}
			//else ... ;// pas de fichier choisi
		}
		
		if (source == view.buttonTextColor){
			Color color = JColorChooser.showDialog (null, "Couleur du texte", model.getTextColor());
			MyColor textColor = new MyColor(color.getRed(), color.getGreen(), color.getBlue());
			if(textColor != null) {
				sendMessage(new Message(Message.CHANGE_TEXT_COLOR, textColor));
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendMessage(Message message) {
		switch(message.getType()) {
			case Message.MESSAGE:
				model.sendMessage(message);
				log("Envoi d'un message non crypté :\n" +
						"\t\t\t    " + view.getSaisie().getText());
				view.getSaisie().setText(""); // On efface la zone de saisie
			break;
			
			case Message.CRYPTED_MESSAGE:
				model.sendMessage(message);
				log("Envoi d'un message crypté :\n" +
						"\t\t\t    " + view.getSaisie().getText());

				view.getSaisie().setText(""); // On efface la zone de saisie
			break;
			
			case Message.DECONNECTION:
				model.sendMessage(message);
				listeningClientThread.stop(); // On stop le thread d'écoute
				model.stop();
				log("Déconnexion");
				System.exit(0);
			break;
			
			case Message.CHANGE_TEXT_COLOR:
				model.sendMessage(message);
				Color color = new Color(message.getColor().getR(), message.getColor().getG(), message.getColor().getB());
				model.setTextColor(color);
				view.getSaisie().setForeground(model.getTextColor());
				log("Changement de couleur du texte");
			break;
		} 
	}
	
	public void receivedMessage(Message message) {
		switch(message.getType()) {
			case Message.NOTIFICATION:	// Réception d'une notification
				String notification = message.getMessage();
				try {
					// Define a keyword attribute
					SimpleAttributeSet keyWord = new SimpleAttributeSet();
					StyleConstants.setBold(keyWord, true);
					StyleConstants.setItalic(keyWord, true);
					StyleConstants.setFontFamily(keyWord, "Courier");
					StyleConstants.setFontSize(keyWord, 10);
					StyleConstants.setForeground(keyWord, Color.BLACK);
					StyleConstants.setAlignment(keyWord, 1);
					view.getDocument().insertString(view.getDocument().getLength(), notification + "\n", keyWord);  // On affiche la notification dans la zone d'affichage
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				log("Réception d'une notification :\n" +
						"\t\t\t    " + notification);
			break;
			
			case Message.MESSAGE:	// Réception d'un message non crypté
				String msg = message.getMessage();
				Color messageTextColor = new Color(message.getColor().getR(), message.getColor().getG(), message.getColor().getB());
				try {
					SimpleAttributeSet style = new SimpleAttributeSet();
					StyleConstants.setBold(style, true);
					StyleConstants.setFontFamily(style, "Courier");
					StyleConstants.setFontSize(style, 12);
					StyleConstants.setForeground(style, Color.BLACK);
					StyleConstants.setAlignment(style, 1);
					view.getDocument().insertString(view.getDocument().getLength(), message.getName() + " dit : ", style); // On affiche le message dans la zone d'affichage
					
					SimpleAttributeSet style2 = new SimpleAttributeSet();
					StyleConstants.setFontFamily(style2, "Courier");
					StyleConstants.setFontSize(style2, 12);
					StyleConstants.setForeground(style2, messageTextColor);
					StyleConstants.setAlignment(style2, 1);
					view.getDocument().insertString(view.getDocument().getLength(), msg + "\n", style2);  // On affiche le message dans la zone d'affichage
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				log("Réception d'un message non crypté :\n" +
						"\t\t\t    " + msg);
			break;
			
			case Message.CRYPTED_MESSAGE: // Réception d'un message crypté
				String[] crypted_message = message.getMsg();
				/*
				 * TODO Réception d'un message crypté sous forme de String
				 */
				String str_crypted_message = model.getRsa().decrypt(crypted_message);
				Color cryptedMessageTextColor = new Color(message.getColor().getR(), message.getColor().getG(), message.getColor().getB());
				try {
					SimpleAttributeSet style = new SimpleAttributeSet();
					StyleConstants.setBold(style, true);
					StyleConstants.setFontFamily(style, "Courier");
					StyleConstants.setFontSize(style, 12);
					StyleConstants.setForeground(style, Color.BLACK);
					StyleConstants.setAlignment(style, 1);
					view.getDocument().insertString(view.getDocument().getLength(), message.getName() + " dit : ", style); // On affiche le message dans la zone d'affichage
					
					SimpleAttributeSet style2 = new SimpleAttributeSet();
					StyleConstants.setFontFamily(style2, "Courier");
					StyleConstants.setFontSize(style2, 12);
					StyleConstants.setForeground(style2, cryptedMessageTextColor);
					StyleConstants.setAlignment(style2, 1);
					view.getDocument().insertString(view.getDocument().getLength(), str_crypted_message + "\n", style2);  // On affiche le message dans la zone d'affichage
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				log("Réception d'un message crypté :\n" +
						"\t\t\t    " + str_crypted_message);
			break;
			
			case Message.CONNECTION: // Réception de la clé public du serveur
				model.setServerPublicKey(message.getPublicKey());
				log("Réception de la clé publique du server :\n" +
						"\t\t\t    " + "e : " + message.getPublicKey().getE() + "\n" +
						"\t\t\t    " + "n : " + message.getPublicKey().getN());
				log("Connexion au serveur");
				view.getSaisie().setForeground(model.getTextColor());
			break;
			
			case Message.LIST_CLIENTS:
				// permet de mettre à jour sa vue (cases à cocher) 
				model.setListe(message.getListe());
				updateListeClient();
				ArrayList<Client> liste = message.getListe();
				if (liste.size() >= 1 ) {
					log("Réception de la liste des clients connectés : " + liste.size() + " clients connectés :");
					for(int i = 0; i < liste.size(); ++i)
						log("\t\t\t    " + "- " + liste.get(i).getName());
				}
				else
					log("Réception de la liste des clients connectés : Aucun client connecté");
			break;
			
			case Message.NEW_CLIENT:
				int idClient = message.getId();
				model.sendMessage(new Message(Message.ENABLE_CLIENT, idClient));
			break;
		}
		//view.scroll_affichage.getVerticalScrollBar().setValue(view.scroll_affichage.getVerticalScrollBar().getMaximum()); // Permet d'ajuster automatiquement la scrollBar vers le bas
	}

	public void updateListeClient() {
		view.getListeClient().removeAll();
		view.getListeClient().setLayout(new BoxLayout(view.getListeClient(), BoxLayout.Y_AXIS));
		
		ArrayList<Client> liste = model.getListe();
		JCheckBox[] tab = new JCheckBox[liste.size()];
		for(int i = liste.size(); --i >= 0;) {
			final int id = liste.get(i).getId();
			final String name = liste.get(i).getName();
			tab[i] = new JCheckBox(liste.get(i).getName());
			Color color = new Color(liste.get(i).getTextColor().getR(), liste.get(i).getTextColor().getG(), liste.get(i).getTextColor().getB());
			tab[i].setForeground(color);
			tab[i].setSelected(true);
			tab[i].addItemListener(new ItemListener() {
				
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						model.sendMessage(new Message(Message.DISABLE_CLIENT, id));
						log("Désactivation du client " + name);
					}
					if (e.getStateChange() == ItemEvent.SELECTED) {
						model.sendMessage(new Message(Message.ENABLE_CLIENT, id));
						log("Activation du client " + name);
					}
					
				}
			});

			view.getListeClient().add(tab[i]);
		}
		
		view.getListeClient().revalidate();
		view.getListeClient().repaint();		
	}
	
	private void log(String log) {
		if (displayLog) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("["+ dateFormat.format(date) + "] - " +log);
		}
	}
}
