package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.model.Login;
import client.view.LoginWindow;

public class LoginController implements ActionListener {
	Login login; 
	LoginWindow loginWindow;
	
	public LoginController(Login login, LoginWindow loginWindow) {
		this.login = login;
		this.loginWindow = loginWindow;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == loginWindow.connexion) {
			if (loginWindow.nom.getText().equals("") ) {
				System.out.println("Vous devez saisir un nom");
				// TODO Afficher message d'erreur, function check
			}
			else {
				login.setName(loginWindow.nom.getText());
				login.setServer(loginWindow.serveur.getText());
				login.setPort(Integer.parseInt(loginWindow.port.getText()));
				loginWindow.dispose();
				login.startClientChat();
			}
		}
	}

}
