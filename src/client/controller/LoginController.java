package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.model.Login;
import client.view.LoginWindow;

public class LoginController implements ActionListener, KeyListener{
	Login model; 
	LoginWindow view;
	
	public LoginController(Login model, LoginWindow view) {
		this.model = model;
		this.view = view;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == view.cancel) {
			System.exit(0);
		}
		if (source == view.connection) {
			if (view.name.getText().equals("") ) {
				System.out.println("Vous devez saisir un nom");
				// TODO Afficher message d'erreur, function check
			}
			else {
				model.setName(view.name.getText());
				model.setServer(view.server.getText());
				model.setPort(Integer.parseInt(view.port.getText()));
				int size = Integer.parseInt((String) view.keySize.getSelectedItem());
				view.dispose();
				model.startClientChat();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (view.name.getText().equals("") ) {
				System.out.println("Vous devez saisir un nom");
				// TODO Afficher message d'erreur, function check
			}
			else {
				model.setName(view.name.getText());
				model.setServer(view.server.getText());
				model.setPort(Integer.parseInt(view.port.getText()));
				int size = Integer.parseInt((String) view.keySize.getSelectedItem());
				view.dispose();
				model.startClientChat();
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
}
