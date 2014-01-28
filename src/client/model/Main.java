package client.model;

public class Main {
	
	public static void main(String[] args)
	{
		RSA rsa_encryption = new RSA();
		rsa_encryption.generateKeys();
		
		System.out.println("############### CLE PUBLIC ###############");
		System.out.println("n = " + rsa_encryption.getPublic_key().getN());
		System.out.println("e = " + rsa_encryption.getPublic_key().getE());
		
		System.out.println("############### CLE PRIVEE ###############");
		System.out.println("n = " + rsa_encryption.getPrivate_key().getN());
		System.out.println("u = " + rsa_encryption.getPrivate_key().getU());
	}
}
