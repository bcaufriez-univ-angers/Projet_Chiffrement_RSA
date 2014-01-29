package client.model;

import client.controller.ClientChatController;
import client.view.ClientChatWindow;

public class Login {
	private String name;
	private String server;
	private int port;
	
	public Login(String name, String server, int port) {
		this.name = name;
		this.server = server;
		this.port = port;
	}
	
	public Login() {
		this.name = "client";
		this.server = "localhost";
		this.port = 30970;
	}
	
	public void startClientChat() {
		ClientChat clientChat = new ClientChat(server, port, name);
		ClientChatWindow clientChatWindow = new ClientChatWindow(clientChat);
		ClientChatController clientChatController = new ClientChatController(clientChat, clientChatWindow);
		
		clientChatWindow.addController(clientChatController);
		
		clientChatWindow.setVisible(true);
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
}
