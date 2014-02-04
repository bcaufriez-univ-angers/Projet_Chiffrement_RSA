package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import common.RSA;

public class Server {
	private static int uniqueId;
	ServerSocket server;
	ArrayList<DialogThread> listeClient;
	RSA rsa;
	int numPort;
	
	public Server(int numPort) {
		this.numPort = numPort;
		listeClient = new ArrayList<DialogThread>();
		rsa = new RSA();
	}
	
	public void start() {
		try {
			server = new ServerSocket(numPort);
			Socket client;
			
			// Création des clés privée/publique du serveur
			rsa.generateKeys();
			System.out.println("Server Public Key : ");
			System.out.println("e : " + rsa.getPublic_key().getE());
			System.out.println("n : " + rsa.getPublic_key().getN());
			
			System.out.println("Chat server is running...");
			while (true) {
				client = server.accept();
				
				DialogThread t = new DialogThread(client, ++uniqueId, listeClient, rsa);
				listeClient.add(t);
				t.start();
			}
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
	
	public void stop() {
		try{
			System.out.println("Chat server is shutting down...");
			server.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}
