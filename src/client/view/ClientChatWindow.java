package client.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.controller.ClientChatController;
import client.model.ClientChat;

public class ClientChatWindow extends JFrame {
	//Client client;
	
	JPanel panelPrincipal;
	JSplitPane splitPaneVert, splitPaneHoriz;
	
	JPanel listeClient = new JPanel();
	JScrollPane scroll_listeClient = new JScrollPane(listeClient);

	JTextArea affichage = new JTextArea();
    JScrollPane scroll_affichage = new JScrollPane(affichage);
    
    JTextArea saisie = new JTextArea();
    JScrollPane scroll_saisie = new JScrollPane(saisie);
    
    JPanel PanelBouton = new JPanel();
    public JButton envoyer = new JButton("Envoyer");
    public JButton quitter = new JButton("Quitter");
    FlowLayout boutons = new FlowLayout();
	
	public ClientChatWindow(ClientChat clientChat)	{
		super("Chat : "+ clientChat.getNomClient());
		this.setTitle("Chat : "+ clientChat.getNomClient());
		this.setSize(500, 500);
		this.setMinimumSize(new Dimension(500, 500));
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //client = new Client(serveur, port, nom, this);
	    
	    panelPrincipal = new JPanel();
	    
	    // quitter.addActionListener(this);
	    // envoyer.addActionListener(this);
	    PanelBouton.setLayout(new FlowLayout());
	    	PanelBouton.add(quitter);
	    	PanelBouton.add(envoyer);	
	    
	    saisie.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
	    //saisie.addKeyListener(this);
	    saisie.setLineWrap(true);
	    saisie.setWrapStyleWord(false);
	    affichage.setLineWrap(true);
	    affichage.setWrapStyleWord(false);
	    affichage.setEnabled(false);	// désactive la saisie dans le JTextArea affichage
	    affichage.setDisabledTextColor(Color.BLUE); // couleur du texte affiché
	    
	    splitPaneHoriz = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listeClient, scroll_affichage);
	    splitPaneHoriz.setDividerLocation(100);
	    	listeClient.setMinimumSize(new Dimension(100, 0));
	    	scroll_affichage.setMinimumSize(new Dimension(200, 0));

	    
	    splitPaneVert = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneHoriz, scroll_saisie);
	    splitPaneVert.setDividerLocation(330);
	    	scroll_saisie.setMinimumSize(new Dimension(0, 95));
	    
		panelPrincipal.setLayout(new BorderLayout());
			panelPrincipal.add(splitPaneVert, BorderLayout.CENTER);
			panelPrincipal.add(PanelBouton, BorderLayout.SOUTH);
		
		this.setContentPane(panelPrincipal);
	    //this.setVisible(true);
	}
	
	public void addController(ClientChatController controller) {
		quitter.addActionListener(controller);
	    envoyer.addActionListener(controller);
	    saisie.addKeyListener(controller);
	}
	/*
	public void refreshListeClient(ArrayList<Contact> liste) {
		listeClient.removeAll();
		listeClient.setLayout(new BoxLayout(listeClient, BoxLayout.Y_AXIS));
		
		JCheckBox[] tab = new JCheckBox[liste.size()];
		for(int i=0; i< liste.size(); i++) {
			final int id = liste.get(i).getId();
			tab[i] = new JCheckBox(liste.get(i).getNom());
			tab[i].setSelected(true);
			tab[i].addItemListener(new ItemListener() {
				
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						System.out.println("Id du client désactivé: "+id);
						send(new Message(Message.DESACTIVER_CLIENT, id));
					}
					if (e.getStateChange() == ItemEvent.SELECTED) {
						System.out.println("Id du client activé: "+id);
						send(new Message(Message.ACTIVER_CLIENT, id));
					}
					
				}
			});

			listeClient.add(tab[i]);
		}
		listeClient.revalidate();
		listeClient.repaint();
	}

	public void send(Message msg){
		client.sendMessage(msg); 	// envoi du message au serveur
		saisie.setText("");						// supprime le texte de la zone de saisie
	}
	
	public void setAffichage(String msg) {
		affichage.append(msg+"\n");				// permmet d'afficher les messages recu
	}
	
	public void quitter() {
		client.sendMessage(new Message(Message.DECONNEXION, ""));
		client.close();
		System.exit(0);
	}
	
	public void start(String clientNom, String addServeur, int port, ClientChatWindow fen) {
		//Client cli = new Client(addServeur, port, clientNom, fen);
		//this.connectToClientProcess(cli);
		//cli.start();
		this.repaint();
	}
	
	public void connectToClientProcess(Client client) {
		this.client = client;
	}
	

	public void setIdConnection(String clientNom, String addServeur, int port) {
		//connectToClientProcess(new Client(addServeur, port, clientNom, this));
		//this.client = new Client(addServeur, port, clientNom, this);
		//this.client.start();
		
	}
	*/
}
