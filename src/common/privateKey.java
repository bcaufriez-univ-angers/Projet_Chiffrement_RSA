package common;

import java.math.BigInteger;

public class privateKey {
	private BigInteger n, u;
	
	public privateKey() {
		this.n = BigInteger.ZERO;
		this.u = BigInteger.ZERO;
	}
	
	public privateKey(BigInteger n, BigInteger u) {
		this.n = n;
		this.u = u;
	}
	
	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getU() {
		return u;
	}

	public void setU(BigInteger u) {
		this.u = u;
	}
}
