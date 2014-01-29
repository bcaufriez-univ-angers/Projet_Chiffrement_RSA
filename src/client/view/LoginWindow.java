package client.view;

import client.controller.LoginController;
import client.model.Login;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginWindow extends JDialog {
	
	public JLabel labelNom;
	public JTextField nom;
	//String nomClient;
	public JLabel labelServeur;
	public JTextField serveur;
	//String addServeur;
	public JLabel labelPort;
	public JTextField port;
	//int numPort;
	public JButton connexion;
	
	public LoginWindow(Login login) {
		this.setTitle("Connexion Chat");
		this.setSize(300, 300);
		this.setAlwaysOnTop(true);
		this.isResizable();
		this.setModal(true);
		
		this.setLayout(new FlowLayout());
			//System.out.println("Constructor View");
			labelNom = new JLabel("Nom : ");
			nom = new JTextField(10);
			labelServeur = new JLabel("Serveur : ");
			serveur = new JTextField("localhost",15);
			labelPort = new JLabel("Port : ");
			port = new JTextField("30970",6);
			connexion = new JButton("Connexion");
			//connexion.addActionListener(this);
			
			this.add(labelNom);
			this.add(nom);
			this.add(labelServeur);
			this.add(serveur);
			this.add(labelPort);
			this.add(port);
			this.add(connexion);
			
			pack();
			//System.out.println("END Constructor View");
			//setVisible(true);
			
	}
	/*
	public void actionPerformed(ActionEvent e) {
		nomClient = nom.getText();
		addServeur = serveur.getText();
		numPort = Integer.parseInt(port.getText());
		
		this.dispose(); // détruit la boite de dialogue
	}*/
	
	public void addController(LoginController controller){
		connexion.addActionListener(controller);
	}

	/*
	public Client getClient() {
		System.out.println(addServeur + " " + numPort + " " + nomClient);
		return new Client(addServeur, numPort, nomClient);
	}
	
	public String getNomClient() {
		return nomClient;
	}
	*/
	/*
	public static void main(String args[]) {
		
		Connexion dialogueConnexion = new Connexion();
		dialogueConnexion.setVisible(true);
		
		FenetreClient2 fen = new FenetreClient2(dialogueConnexion.getNomClient());
		Client client = dialogueConnexion.getClient();
		client.setFenetre(fen);
		fen.connectToClientProcess(client);
		client.start();
		
	}
	*/
}
