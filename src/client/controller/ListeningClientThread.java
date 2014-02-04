package client.controller;

import client.model.ClientChat;
import common.Message;


public class ListeningClientThread extends Thread {

	ClientChatController controller;
	ClientChat clientChat;
	
	public ListeningClientThread(ClientChatController controller, ClientChat clientChat) {
		this.controller = controller;
		this.clientChat = clientChat;
	}

	public void run() {
		try {			
			while (true) {
				try {
					Message message = (Message) clientChat.getIn().readObject();
					controller.receivedMessage(message);
				}
				catch(Exception e) {
					System.out.println("Exception ecoute du serveur: " + e);
				}
			}
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}	
}
