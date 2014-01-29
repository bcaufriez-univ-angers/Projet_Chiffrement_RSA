package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static int uniqueId;
	ServerSocket server;
	ArrayList<DialogThread> listeClient;
	int numPort;
	
	public Server(int numPort) {
		this.numPort = numPort;
		listeClient = new ArrayList<DialogThread>();
	}
	
	public void start() {
		try {
			server = new ServerSocket(numPort);
			Socket client;
			
			System.out.println("Chat server is running...");
			while (true) {
				client = server.accept();
				
				DialogThread t = new DialogThread(client, ++uniqueId, listeClient);
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
