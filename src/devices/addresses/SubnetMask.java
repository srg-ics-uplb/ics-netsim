package devices.addresses;

import java.io.Serializable;

import java.util.StringTokenizer;

/*
 * TODO: Replace deprecated class StringTokenizer. Use String.split() instead
 */
public class SubnetMask implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7552311886593607478L;
	public static final String CLASS_A_SUBNET_MASK = "255.0.0.0";
    public static final String CLASS_B_SUBNET_MASK = "255.255.0.0";
    public static final String CLASS_C_SUBNET_MASK = "255.255.255.0";
    private int a;
    private int b;
    private int c;
    private int d;

    public SubnetMask() {
    }

    public SubnetMask(String address) {
        StringTokenizer tokens = new StringTokenizer(address, ".");
        a = Integer.parseInt(tokens.nextToken());
        b = Integer.parseInt(tokens.nextToken());
        c = Integer.parseInt(tokens.nextToken());
        d = Integer.parseInt(tokens.nextToken());
    }

    public String toString() {
        return new String(a + "." + b + "." + c + "." + d);
    }

    public void setMask(String address) {
        StringTokenizer tokens = new StringTokenizer(address, ".");
        a = Integer.parseInt(tokens.nextToken());
        b = Integer.parseInt(tokens.nextToken());
        c = Integer.parseInt(tokens.nextToken());
        d = Integer.parseInt(tokens.nextToken());
    }

    public void setMask(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String getMask() {
        return new String(a + "." + b + "." + c + "." + d);
    }

    public int[] getMaskArray() {
        int[] quartet = { a, b, c, d };

        return quartet;
    }
}
