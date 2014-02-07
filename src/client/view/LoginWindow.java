package client.view;

import client.controller.LoginController;
import client.model.Login;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginWindow extends JDialog {
	Login model;
	
	public JPanel panel;
	public JLabel labelName;
	public JTextField name;
	public JLabel labelServer;
	public JTextField server;
	public JLabel labelPort;
	public JTextField port;
	public JLabel labelKeySize;
	public JComboBox keySize;
	public JButton cancel;
	public JButton connection;
	
	public LoginWindow(Login model) {
		this.model = model;
		this.setTitle("Connexion Chat");
		this.setSize(300, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setModal(true);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2, 10, 10));
		labelName = new JLabel("Nom : ");
		name = new JTextField(10);
		labelServer = new JLabel("Serveur : ");
		server = new JTextField("localhost",15);
		labelPort = new JLabel("Port : ");
		port = new JTextField("30970",6);
		labelKeySize = new JLabel("Taille de la clé : ");
		String[] size = {"128", "256", "512", "1024", "2048"};
		keySize = new JComboBox(size);
		cancel = new JButton("Annuler");
		connection = new JButton("Connexion");
		
		panel.add(labelName);
		panel.add(name);
		panel.add(labelServer);
		panel.add(server);
		panel.add(labelPort);
		panel.add(port);
		panel.add(labelKeySize);
		panel.add(keySize);
		panel.add(cancel);
		panel.add(connection);
		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setContentPane(panel);
		this.pack();
	}
	
	public void addController(LoginController controller){
		cancel.addActionListener(controller);
		connection.addActionListener(controller);
		name.addKeyListener(controller);
		server.addKeyListener(controller);
		port.addKeyListener(controller);
		keySize.addKeyListener(controller);
	}
}