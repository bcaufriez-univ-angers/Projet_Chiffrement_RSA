package client.model;

import client.controller.ClientChatController;
import client.view.ClientChatWindow;

public class Login {
	private String name;
	private String server;
	private int port;
	private int keySize;
	
	public Login(String name, String server, int port, int keySize) {
		this.name = name;
		this.server = server;
		this.port = port;
		this.keySize = keySize;
	}
	
	public Login() {
		this.name = "client";
		this.server = "localhost";
		this.port = 30970;
		this.keySize = 128;
	}
	
	public void startClientChat() {
		ClientChat clientChat = new ClientChat(name, server, port, keySize);
		ClientChatWindow clientChatWindow = new ClientChatWindow(clientChat);
		ClientChatController clientChatController = new ClientChatController(clientChat, clientChatWindow);
		
		clientChatWindow.addController(clientChatController);
		
		clientChatController.openSocket();
		
		clientChat.start();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}
}