package client.model;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
	private publicKey public_key;
	private privateKey private_key;
	private BigInteger m = BigInteger.ZERO;
	
	public RSA() {
		this.public_key = new publicKey();
		this.private_key = new privateKey();
	}
	
	public void generateKeys() {
		generatePublicKey();
		generatePrivateKey();
	}

	public void generatePublicKey() {
		BigInteger p = BigInteger.probablePrime(128, new Random());
		BigInteger q = BigInteger.probablePrime(128, new Random());
		
		while (p.compareTo(q) == 0)
		{
			q.nextProbablePrime();
		}
		
		BigInteger n = p.multiply(q);
		m = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));

		BigInteger e = BigInteger.probablePrime(25, new Random());
		
		while(m.gcd(e).compareTo(new BigInteger("1"))!=0)
		{
			e = e.nextProbablePrime();
		}

		public_key = new publicKey(n, e);
	}
	
	public void generatePrivateKey() {
		BigInteger u0 = BigInteger.ONE;
		BigInteger v0 = BigInteger.ZERO;
		
		BigInteger u1 = BigInteger.ZERO;
		BigInteger v1 = BigInteger.ONE;

		BigInteger r0 = public_key.getE();
		BigInteger r1 = m;

		BigInteger u = u0.subtract(r0.divide(r1).multiply(u1));
		BigInteger v = v0.subtract(r0.divide(r1).multiply(v1));
		BigInteger r = r0.subtract(r0.divide(r1).multiply(r1));

		while(r.compareTo(new BigInteger("0")) != 0) {	
			r0 = r1;
			r1 = r;
			u0 = u1;
			u1 = u;
			v0 = v1;
			v1 = v;
			u = u0.subtract((r0.divide(r1)).multiply(u1));
			v = v0.subtract((r0.divide(r1)).multiply(v1));
			r= r0.subtract((r0.divide(r1)).multiply(r1));
		}
		u = u1;
		v = v1;
		
		private_key = new privateKey(public_key.getN(), u);
	}
	
	public String[] encrypt(String message, publicKey public_key) {
		String[] res = new String[message.length()];

		for(int i = 0 ;i < message.length(); i++)
		{
			int charVal = (int)message.charAt(i);
			BigInteger bigIntVal = new BigInteger(String.valueOf(charVal)).modPow(public_key.getE(), public_key.getN());
			res[i] = bigIntVal.toString();
			System.out.println(res[i]);
		}
		System.out.println();
		return res;
		//return null;
	}
	
	private String implode(String[] args){
		StringBuffer sb = new StringBuffer();
		
		for(int i =0; i < args.length; i++){
			sb.append(args[i]);
		}
		
		return sb.toString();
	}
	
	public String decrypt(String[] message) {
		String[] res = new String[message.length];
		for(int i = 0; i < message.length; i++)
		{
			BigInteger bigIntVal = new BigInteger(message[i]);
			String stringVal = bigIntVal.modPow(private_key.getU(), private_key.getN()).toString();
			int val = Integer.parseInt(stringVal);
			res[i] = String.valueOf((char)val);
			//System.out.println(res[i]);
		}
		
		return implode(res);
	}

	public publicKey getPublic_key() {
		return public_key;
	}

	public void setPublic_key(publicKey public_key) {
		this.public_key = public_key;
	}

	public privateKey getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(privateKey private_key) {
		this.private_key = private_key;
	}
}
