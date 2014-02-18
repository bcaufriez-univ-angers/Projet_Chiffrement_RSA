package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyleContext;

import client.controller.ClientChatController;
import client.model.ClientChat;

public class ClientChatWindow extends JFrame {
	ClientChat model;
	
	JPanel panelPrincipal;
	JSplitPane splitPaneVert, splitPaneHoriz;
	
	JPanel listeClient = new JPanel();
	JScrollPane scroll_listeClient = new JScrollPane(listeClient);

	JTextPane affichage = new JTextPane();
	Document document = affichage.getDocument();
	public JScrollPane scroll_affichage = new JScrollPane(affichage);
    
    JTextArea saisie = new JTextArea();
    JScrollPane scroll_saisie = new JScrollPane(saisie);
    
    JPanel PanelBouton = new JPanel();
    public JButton quitter = new JButton("Quitter");
    public JButton envoyer = new JButton("Envoyer");
    public JButton buttonTextColor = new JButton(new ImageIcon(getClass().getResource("/client/icon/buttonTextColor.png")));
    public JButton buttonSendFile = new JButton(new ImageIcon(getClass().getResource("/client/icon/buttonSendFile.png")));
    public JCheckBox buttonEncrypt = new JCheckBox(new ImageIcon(getClass().getResource("/client/icon/buttonEncrypt.png")));
    
    FlowLayout boutons = new FlowLayout();
	
	public ClientChatWindow(ClientChat model) {
		super("Chat : " + model.getName());
		this.model = model;
		this.setTitle("Chat : "+ model.getName());
		this.setSize(500, 500);
		this.setMinimumSize(new Dimension(500, 500));
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    panelPrincipal = new JPanel();
	    
	    buttonSendFile.setContentAreaFilled(false);
	    buttonSendFile.setBorderPainted(false);
	    buttonSendFile.setMargin(new Insets(1,1,1,1)); 
	    buttonTextColor.setContentAreaFilled(false);
	    buttonTextColor.setBorderPainted(false);
	    buttonTextColor.setMargin(new Insets(1,1,1,1)); 
	    
	    PanelBouton.setLayout(new FlowLayout());
	    	PanelBouton.add(quitter);
	    	PanelBouton.add(envoyer);
	    	//PanelBouton.add(buttonSendFile);
	    	PanelBouton.add(buttonTextColor);
	    	PanelBouton.add(buttonEncrypt);
	    
	    buttonEncrypt.setSelected(true);
	    
	    saisie.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
	    saisie.setLineWrap(true);
	    saisie.setWrapStyleWord(false);
	    affichage.setEditable(false);	// désactive la saisie dans le JTextArea affichage
	    
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
	    buttonSendFile.addActionListener(controller);
	    buttonTextColor.addActionListener(controller);
	    buttonEncrypt.addItemListener(controller);
	    saisie.addKeyListener(controller);
	}

	public JTextPane getAffichage() {
		return affichage;
	}

	public Document getDocument() {
		return document;
	}

	public void setAffichage(JTextPane affichage) {
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