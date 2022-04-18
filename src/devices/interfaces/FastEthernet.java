package devices.interfaces;

import devices.Device;


public class FastEthernet extends Ethernet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3127711737233263363L;
	private final String description = "\n   Hardware is AmdFE, address is 00b0.1030.3f30 (bia 00b0.1030.3f30)\n   MTU 1500 bytes, BW 100000 Kbit, DLY 100 usec,\n      reliablility 255/255, txload 1/255, rxload 1/255\n   Encapsulation ARPA, loopback not set\n   Keepalive set (10)\n   Full -duplex, 100Mb/s, 100BaseTX/FX\n   ARP type: ARPA, ARP Timeout 04:00:00\n   Last input 00:00:50, output 00:00:04, output hang never\n   Last clearing of \"show interface\" counters never\n   Queueing strategy: fifo\n   Output queue 0/40, 0 drops; input queue 0/75, 0 drops\n   5 minute input rate 0 bits/sec, 0 packets/sec\n   5 minute output rate 1000 bits/sec, 0 packets/sec\n      588 packets input, 74628 bytes\n      Received 588 broadcasts, 0 runts, 0 giants, 0 throttles\n      0 input errors, 0 CRC, 0 frame, 0 overrun, 0 ignored\n      0 watchdog, 0 multicast\n      0 input packets with dribble condition detected\n      231 packets output, 53712 bytes, 0 underruns\n      0 output errors, 0 collisions, 1 interface resets\n      0 babbles, 0 late collision, 0 deferred\n      0 lost carrier, 0 no carrier\n      0 output buffer failures, 0 output buffers swapped out";
    private boolean trunked = false;
    
    public FastEthernet(Device device, String name, String consoleName) {
        super(device, name, consoleName);
    }

    public String getDescription() {
        return description;
    }
    
    public void setTrunked(boolean trunked){
    	this.trunked = trunked;
    }
    
    public boolean isTrunked(){
    	return trunked;    	
    }
}
