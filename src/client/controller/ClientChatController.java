package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.model.ClientChat;
import client.model.Login;
import client.view.ClientChatWindow;
import client.view.LoginWindow;

import common.Message;

public class ClientChatController implements ActionListener, KeyListener {
	
	ClientChat model;
	ClientChatWindow view; 
	
	public ClientChatController(ClientChat model, ClientChatWindow view) {
		System.out.println("Constructor Client Chat Controller");
		this.model = model;
		this.view = view;
	}
	
	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("Touche ENTER");
			// send(new Message(Message.MESSAGE, saisie.getText()));
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == view.quitter){
			System.out.println("click button quitter");
			//quitter();
		}
		
		if (source == view.envoyer){
			System.out.println("click button envoyer");
			//send(new Message(Message.MESSAGE, saisie.getText()));
		}
	}

}
