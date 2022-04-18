package platform.gui;


import devices.packets.*;
import devices.packets.tcp.*;
import devices.packets.udp.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class PacketView extends JFrame {
	
	public JTextArea textArea;
	public JScrollPane scrollPane;
	public JPanel mainPanel;
	public String stringTemp;
	
	public PacketView(String title, EthernetPacket packet){
		super();
		try{
        	this.setAlwaysOnTop(true);
        }catch(Exception e){}
        frameInit();
        setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(620,600));
        this.setPreferredSize(new Dimension(620,600));
        setTitle(title);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        textArea = new JTextArea(60, 70);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        textArea.setFont(new Font(Font.MONOSPACED, 1, 12));
        
        if (packet instanceof EthernetPacket){
        	textArea.append("  Ethernet 802.3\n\n");
        	textArea.append("  Note: Size headings are in BYTES\n");
        	textArea.append("  0                   5                   10                  15                  20\n");
        	textArea.append("  0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16  17  18  19  20\n");
        	textArea.append("  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
        	textArea.append("  |           PREAMBLE:           |        Dest. MAC      |       Source MAC      |\n");
        	textArea.append("  |        101010...1011          |" + correctString(packet.getSrcMAC(), 23) + "|" + correctString(packet.getDestMAC(), 23) + "|\n");
        	textArea.append("  +-------------------------------------------------------------------------------+\n");
        	textArea.append("  | TYPE: |                        Data                           |      FCS:     |\n");
        	textArea.append("  |" + correctString(packet.getType(), 7) + "|                   (Variable Length)                   |" + correctString(packet.getFcs(), 15) + "|\n");
        	textArea.append("  +-------------------------------------------------------------------------------+\n\n\n");

        	if (packet instanceof EthernetIPPacket){
        		EthernetIPPacket ipPacket = (EthernetIPPacket) packet;
        		textArea.append("  IP\n\n");
        		textArea.append("  Note: Size headings are in BITS\n");
        		textArea.append("  0                   1                   2                   3\n");
        		textArea.append("  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2\n");
        		textArea.append("  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
        		textArea.append("  |   4   |  IHL  |Type of Service |         Total Length         |\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |" + correctString(("ID: " + ipPacket.getId()), 31) + "| 0x0 |"+ correctString(ipPacket.getFragOffset(),25)+"|\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |" + correctString(("TTL: " + ipPacket.getTtl()),15) + "|" + correctString(("Pro: " + ipPacket.getPro()), 15)  + "|            Checksum           |\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |" + correctString(("Source IP: " + ipPacket.getSourceIp()), 63) + "|\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |" + correctString(("Destination IP: " + ipPacket.getDestIp()), 63) + "|\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |" + correctString(("Opt: " + ipPacket.getOpt()),47) + "|      0x0      |\n");
        		textArea.append("  +---------------------------------------------------------------+\n");
        		textArea.append("  |                     DATA (Variable Length)                    |\n");
        		textArea.append("  +---------------------------------------------------------------+\n\n\n");
        		if (packet instanceof TCPPacket){
        			TCPPacket tcpPacket = (TCPPacket) packet;
        			textArea.append("");
        			textArea.append("  TCP part\n\n");
        			textArea.append("  Note: Size headings are in BITS\n");
        			textArea.append("  0                   1                   2                   3\n");
        			textArea.append("  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2\n");
        			textArea.append("  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
        			textArea.append("  |" + correctString(("Source Port: " + tcpPacket.getSourcePort()), 31) + "|" + correctString(("Dest Port: " + tcpPacket.getDestPort()), 31) + "|\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |" + correctString(("Seq Num: " + tcpPacket.getSeqNum()), 63) + "|\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |" + correctString(("Ack Num: " + tcpPacket.getAckNum()), 63) + "|\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  | Offset|  Reserved | Ctrl Bits |             Window            |\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |" + correctString(("Checksum: " + tcpPacket.getChecksum()), 31) + "|        Urgent Pointer         |\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |                       Option                  |    Padding    |\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |                     DATA (Variable Length)                    |\n");
        			textArea.append("  +---------------------------------------------------------------+\n\n\n");
        			if (packet instanceof HTTPPacket){
        				HTTPPacket httpPacket = (HTTPPacket) packet;
        				textArea.append("  HTTP Data\n\n");
        				textArea.append("  +---------------------------------------------------------------+\n\n");
        				textArea.append(httpPacket.getHTTPData() + "\n\n");
        				textArea.append("  +---------------------------------------------------------------+\n\n");
        			}
        		}
        		if (packet instanceof UDPPacket){
        			UDPPacket udpPacket = (UDPPacket) packet;
        			textArea.append("  UDP\n\n");
        			textArea.append("  Note: Size headings are in BITS\n");
        			textArea.append("  0                   1                   2                   3\n");
        			textArea.append("  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2\n");
        			textArea.append("  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
        			textArea.append("  |" + correctString(("Source Port: " + udpPacket.getSourcePort()), 31) + "|" + correctString(("Destination Port: " + udpPacket.getDestPort()), 31) + "|\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |" + correctString(("Length: " + udpPacket.getLength()), 31) + "|" + correctString(("Checksum: " + udpPacket.getChecksum()), 31) + "|\n");
        			textArea.append("  +---------------------------------------------------------------+\n");
        			textArea.append("  |                     DATA (Variable Length)                    |\n");
        			textArea.append("  +---------------------------------------------------------------+\n\n\n");
        			if (packet instanceof DHCPPacket){
        				DHCPPacket dhcpPacket = (DHCPPacket) packet;
        				textArea.append("  DHCP\n\n");
        				textArea.append("  Note: Size headings are in BITS.\n");
        				textArea.append("        Numbers in parentheses illustrate field size in OCTETS.\n");
        				textArea.append("  0                   1                   2                   3\n");
        				textArea.append("  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2\n");
        				textArea.append("  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
        				textArea.append("  |       Op      |     Htype     |     Hlen      |      Hops     |\n");
        				textArea.append("  +---------------+---------------+---------------+---------------+\n");
        				textArea.append("  |                              xid                              |\n");
        				textArea.append("  +-------------------------------+-------------------------------+\n");
        				textArea.append("  |              Secs             |              Flags            |\n");
        				textArea.append("  +-------------------------------+-------------------------------+\n");
        				textArea.append("  |" + correctString(("Client IP Address: " + dhcpPacket.getCiaddr()), 63) + "|\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        				textArea.append("  |" + correctString(("\"Your\" IP Address: " + dhcpPacket.getYiaddr()), 63) + "|\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        				textArea.append("  |" + correctString(("Next Server Address: " + dhcpPacket.getSiaddr()), 63) + "|\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        				textArea.append("  |" + correctString(("Relay Agent IP Address: " + dhcpPacket.getGiaddr()), 63) + "|\n");
        				textArea.append("  +-------------------------------+-------------------------------+\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  |" + correctString(("Client Hardware Address: " + dhcpPacket.getChaddr() + " (16)"), 63) + "|\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  +-------------------------------+-------------------------------+\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  |                     Server host name  (64)                    |\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  |                          FILE    (128)                        |\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        				textArea.append("  |                                                               |\n");
        				textArea.append("  |                   Options (Variable Length)                   |\n");
        				textArea.append("  +---------------------------------------------------------------+\n");
        			}
        		}
        	}
        }
        mainPanel.add(scrollPane);
        container.add(mainPanel);
        
        pack();
	}

	public String correctString(String s, int fieldLength){
		String tempString = "";
		int tempInt = 0;
		String tokens[] = s.split(" ");
		for (int i = 0; i < (tokens.length-1); i++){
			tempString += tokens[i] + " ";
		}
		tempString += tokens[tokens.length - 1];
		tempInt = fieldLength - tempString.length();
		if (tempInt%2 == 0){
			tempString = generateWhitespace(tempInt/2) + tempString + generateWhitespace(tempInt/2);
		}else{
			tempString = generateWhitespace(tempInt/2) + tempString + generateWhitespace((tempInt/2)+1);
		}
		return tempString;
	}
	
	public String generateWhitespace(int n){
		String spaces = "";
		for (int x = 0; x < n; x++){
			spaces += " ";
		}
		return spaces;
	}
}
