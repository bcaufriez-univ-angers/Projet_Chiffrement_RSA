package server;

public class RunServer {
	
	public static void main(String args[]) {
		Server server = new Server(30970);
		server.start();
	}
}
