package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.RSA;

public class Server {
	private static boolean displayLog = true;
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
			log("Création des clés publique/privée du serveur");
			
			log("Le serveur est en cours d'exécution...");
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
			System.out.println("Le serveur de chat est en cours d'arrêt...");
			server.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
	
	private void log(String log) {
		if (displayLog) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("["+ dateFormat.format(date) + "] - " +log);
		}
	}
}
