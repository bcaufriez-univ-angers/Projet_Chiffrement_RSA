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
	
	void generateKeys() {
		generatePublicKey();
		generatePrivateKey();
	}

	void generatePublicKey() {
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
			System.out.println("on est là");
			e = e.nextProbablePrime();
		}

		public_key = new publicKey(n, e);
	}
	
	void generatePrivateKey() {
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
