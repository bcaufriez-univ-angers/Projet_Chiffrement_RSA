package client.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import common.Contact;

import client.controller.ClientChatController;
import client.model.ClientChat;

public class ClientChatWindow extends JFrame {
	ClientChat model;
	
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
	
	public ClientChatWindow(ClientChat model) {
		super("Chat : " + model.getNomClient());
		this.model = model;
		this.setTitle("Chat : "+ model.getNomClient());
		this.setSize(500, 500);
		this.setMinimumSize(new Dimension(500, 500));
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    panelPrincipal = new JPanel();
	    
	    PanelBouton.setLayout(new FlowLayout());
	    	PanelBouton.add(quitter);
	    	PanelBouton.add(envoyer);	
	    
	    saisie.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
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

		// Permet d'ajuster automatiquement la scrollBar
		scroll_affichage.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
			    e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});
		
		scroll_saisie.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
			    e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});

		this.setContentPane(panelPrincipal);
	    this.setVisible(true);
	}
	
	public void addController(ClientChatController controller) {
		quitter.addActionListener(controller);
	    envoyer.addActionListener(controller);
	    saisie.addKeyListener(controller);
	}

	public JTextArea getAffichage() {
		return affichage;
	}

	public void setAffichage(JTextArea affichage) {
		this.affichage = affichage;
	}

	public JTextArea getSaisie() {
		return saisie;
	}

	public void setSaisie(JTextArea saisie) {
		this.saisie = saisie;
	}

	public JPanel getListeClient() {
		return listeClient;
	}

	public void setListeClient(JPanel listeClient) {
		this.listeClient = listeClient;
	}
	
}
