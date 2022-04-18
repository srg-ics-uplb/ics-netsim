package devices.addresses;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;


/*
 * Note: The StringTokenizer class is deprecated. Replace it with the usage of the String.split() method.
 */

public class IPAddress extends Address {

    

	/**
	 * 
	 */
	private static final long serialVersionUID = -506748813670782604L;
	public static final String DEFAULT_GATEWAY = "0.0.0.0";
    public static final String LOOPBACK = "127.0.0.1";
    public static final String BROADCAST = "255.255.255.255";
    private int a;
    private int b;
    private int c;
    private int d;

    public IPAddress() {
    }

    public IPAddress(String address) {
        StringTokenizer tokens = new StringTokenizer(address, ".");
        a = Integer.parseInt(tokens.nextToken());
        b = Integer.parseInt(tokens.nextToken());
        c = Integer.parseInt(tokens.nextToken());
        d = Integer.parseInt(tokens.nextToken());
    }

    public void setAddress(String address) {
        StringTokenizer tokens = new StringTokenizer(address, ".");
        a = Integer.parseInt(tokens.nextToken());
        b = Integer.parseInt(tokens.nextToken());
        c = Integer.parseInt(tokens.nextToken());
        d = Integer.parseInt(tokens.nextToken());
    }

    public void setAddress(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String toString() {
        return new String(a + "." + b + "." + c + "." + d);
    }

    public int[] getAddress() {
        int[] quartet = { a, b, c, d };

        return quartet;
    }

    public boolean compareIPAdress(int[] ipadd1, int[] ipadd2) {
        for (int i = 0; i < ipadd1.length; i++) {
            if (ipadd1[i] == ipadd2[i]) {
                continue;
            }

            return false;
        }

        return true;
    }

    public static int[] getQuartet(String str) {
        int[] decimals = new int[4];
        StringTokenizer tokens = new StringTokenizer(str, ".");

        for (int i = 0; i < decimals.length; i++) {
            decimals[i] = Integer.parseInt(tokens.nextToken());
        }

        return decimals;
    }

    public static String getClassfulSubnet(String address) {
        String subnet = null;
        int[] decimals = getQuartet(address);
        String firstQuartet = Integer.toBinaryString(decimals[0]);

        for (int i = 0; i < (8 - firstQuartet.length()); i++) {
            firstQuartet = "0" + firstQuartet;
        }

        if (firstQuartet.startsWith("0")) {
            subnet = SubnetMask.CLASS_A_SUBNET_MASK;
        } else if (firstQuartet.startsWith("10")) {
            subnet = SubnetMask.CLASS_B_SUBNET_MASK;
        } else if (firstQuartet.startsWith("110")) {
            subnet = SubnetMask.CLASS_C_SUBNET_MASK;
        }

        return subnet;
    }

    public static boolean isValidInputQuartet(String str) {
        boolean isValidQuartet = true;
        int octetCounter = 0;
        boolean error = false;
        StringBuffer decimalBuffer = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (octetCounter == 4) {
                showQuartetError();
                error = true;
            } else if (ch == '.') {
                if (decimalBuffer.length() > 0) {
                    decimalBuffer = decimalBuffer.delete(0, decimalBuffer.length());
                    octetCounter++;
                } else {
                    showQuartetError();
                    error = true;
                }
            } else {
                if (Character.isDigit(ch)) {
                    decimalBuffer.append(ch);

                    if ((decimalBuffer.length() == 3) || (i == (str.length() - 1))) {
                        int decimal = Integer.parseInt(decimalBuffer.toString());

                        if (!((decimal >= 0) && (decimal <= 255))) {
                            showQuartetError();
                            error = true;
                        } else {
                            if (i == (str.length() - 1)) {
                                octetCounter++;
                            }
                        }
                    } else if (decimalBuffer.length() > 3) {
                        showQuartetError();
                        error = true;
                    }
                } else {
                    showQuartetError();
                    error = true;
                }
            }

            if (error) {
                isValidQuartet = false;

                break;
            }
        }

        if (isValidQuartet) {
            if (octetCounter != 4) {
                showQuartetError();
                isValidQuartet = false;
            }
        }

        return isValidQuartet;
    }

    private static void showQuartetError() {
        String QUARTET_ERROR = "QUARTET ERROR!";
        JOptionPane.showMessageDialog(null, "Error!", QUARTET_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public static String getNetworkAddress(IPAddress address, SubnetMask mask) {
        int[] addressQuartet = address.getAddress();
        int[] maskQuartet = mask.getMaskArray();
        StringBuffer networkAddress = new StringBuffer();

        for (int i = 0; i < addressQuartet.length; i++) {
            networkAddress.append(addressQuartet[i] & maskQuartet[i]);

            if (i != (addressQuartet.length - 1)) {
                networkAddress.append(".");
            }
        }

        return networkAddress.toString();
    }
}
