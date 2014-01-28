package client.model;

public class Main {
	
	public static void main(String[] args)
	{
		RSA alice = new RSA();
		alice.generateKeys();
		System.out.println("############### ALICE CLE PUBLIC ###############");
		System.out.println("n = " + alice.getPublic_key().getN());
		System.out.println("e = " + alice.getPublic_key().getE());
		
		System.out.println("############### ALIVE CLE PRIVEE ###############");
		System.out.println("n = " + alice.getPrivate_key().getN());
		System.out.println("u = " + alice.getPrivate_key().getU());
		System.out.println();
		
		RSA bob = new RSA();
		bob.generateKeys();
		System.out.println("############### BOB CLE PUBLIC ###############");
		System.out.println("n = " + bob.getPublic_key().getN());
		System.out.println("e = " + bob.getPublic_key().getE());
		
		System.out.println("############### BOB CLE PRIVEE ###############");
		System.out.println("n = " + bob.getPrivate_key().getN());
		System.out.println("u = " + bob.getPrivate_key().getU());
		System.out.println();
		
		System.out.println("############### ALICE ENCRYPT ################");
		String message[];
		message = alice.encrypt("Bonjour le monde !", bob.getPublic_key());
		System.out.println("################ BOB DECRYPT #################");
		System.out.println(bob.decrypt(message));
	}
}
