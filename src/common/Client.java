package common;

import java.io.Serializable;

public class Client implements Serializable {

	private static final long serialVersionUID = 7102572958044670972L;
	
	public static final int COMPUTER = 0;
	public static final int MOBILE = 1;

	private int id;
	private String name;
	private publicKey publicKey;
	private MyColor textColor;
	private int deviceType;
	
	public Client(String name, publicKey publicKey, MyColor textColor, int deviceType) {
		this.name = name;
		this.publicKey = publicKey;
		this.textColor = textColor;
		this.deviceType = deviceType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public publicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(publicKey publicKey) {
		this.publicKey = publicKey;
	}

	public MyColor getTextColor() {
		return textColor;
	}

	public void setTextColor(MyColor textColor) {
		this.textColor = textColor;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
}
