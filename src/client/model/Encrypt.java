package Client;

import java.math.BigInteger;
import java.util.Random;

public class Encrypt {

	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger m;
	private BigInteger e;
	private BigInteger u;
	private BigInteger v;

	private String privateKey;
	private String publicKey;

	public Encrypt()
	{
		p = BigInteger.probablePrime(512, new Random());
		q = BigInteger.probablePrime(512, new Random());

		while (p.compareTo(q) == 0)
		{
			System.out.println("on est là");
			q.nextProbablePrime();
		}

		n = p.multiply(q);
		m = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));

		e = BigInteger.probablePrime(50, new Random());

		while(m.gcd(e).compareTo(new BigInteger("1"))!=0)
		{
			System.out.println("on est là");
			e = e.nextProbablePrime();
		}
		publicKey = n.toString()+","+e.toString();

		BigInteger u0 = new BigInteger("1");
		BigInteger v0 = new BigInteger("0");
		
		BigInteger u1 = new BigInteger("0");
		BigInteger v1 = new BigInteger("1");

		BigInteger r0 = e;
		BigInteger r1 = m;

		u = u0.subtract(r0.divide(r1).multiply(u1));
		v = v0.subtract(r0.divide(r1).multiply(v1));
		BigInteger r = r0.subtract(r0.divide(r1).multiply(r1));

		
		while(r.compareTo(new BigInteger("0")) != 0)
		{
	
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
		
		privateKey = n.toString()+","+u.toString();
	}

	public BigInteger getP()
	{
		return this.p;
	}


	public String[] crypt(String str)
	{
		String[] res = new String[str.length()];

		for(int i=0;i<str.length();i++)
		{
			int charVal = (int)str.charAt(i);
			BigInteger bigIntVal = new BigInteger(String.valueOf(charVal)).modPow(e, n);
			res[i] = bigIntVal.toString();
			System.out.println(res[i]);
		}
		System.out.println();
		return res;
	}
	
	public static String implode(String[] args){
		StringBuffer sb = new StringBuffer();
		
		for(int i =0; i < args.length; i++){
			sb.append(args[i]);
		}
		
		return sb.toString();
	}

	public String decrypt(String[] str)
	{
		String[] res = new String[str.length];
		for(int i=0;i<str.length;i++)
		{
			BigInteger bigIntVal = new BigInteger(str[i]);
			String stringVal = bigIntVal.modPow(u, n).toString();
			int val = Integer.parseInt(stringVal);
			res[i] = String.valueOf((char)val);
			//System.out.println(res[i]);
		}
		
		return implode(res);
	}

	public String getPrivateKey()
	{
		return this.privateKey;
	}
	
	public String getPublicKey()
	{
		return this.publicKey;
	}
	
	public static void main(String[] args)
	{
		Encrypt enc = new Encrypt();
		System.out.println(enc.decrypt(enc.crypt("salut tous les gens!")));
	}
}