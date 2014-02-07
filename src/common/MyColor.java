package common;

import java.io.Serializable;

public class MyColor implements Serializable {
	private static final long serialVersionUID = 5866327343059390527L;
	
	private int r;
	private int g;
	private int b;
	
	public MyColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
}
