package client.model;

import java.math.BigInteger;

public class publicKey {
	private BigInteger n, e;
	
	public publicKey() {
		this.n = BigInteger.ZERO;
		this.e = BigInteger.ZERO;
	}
	
	public publicKey(BigInteger n, BigInteger e) {
		this.n = n;
		this.e = e;
	}
	
	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getE() {
		return e;
	}

	public void setE(BigInteger e) {
		this.e = e;
	}
}
