package common;

import java.math.BigInteger;
import java.io.Serializable;

public class publicKey implements Serializable {
	
	private static final long serialVersionUID = 5614021491089241020L;
	
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
