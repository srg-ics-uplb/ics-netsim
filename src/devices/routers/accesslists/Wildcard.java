package devices.routers.accesslists;

import java.io.Serializable;
import java.util.StringTokenizer;

public class Wildcard implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3550841787463256959L;
	public static final String CLASS_A_SUBNET_MASK = "255.0.0.0";
    public static final String CLASS_B_SUBNET_MASK = "255.255.0.0";
    public static final String CLASS_C_SUBNET_MASK = "255.255.255.0";
    private int a;
    private int b;
    private int c;
    private int d;

    public Wildcard() {
    }

    public Wildcard(String address) {
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
