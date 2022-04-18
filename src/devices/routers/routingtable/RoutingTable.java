package devices.routers.routingtable;

import java.io.Serializable;

import java.util.Vector;


public class RoutingTable implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5816274881630873553L;
	private final Vector table = new Vector();

    public void addEntry(Entry entry) {
        table.add(entry);
    }

    public Entry[] getEntries() {
        Entry[] entries = new Entry[table.size()];

        for (int i = 0; i < entries.length; i++) {
            entries[i] = (Entry) table.get(i);
        }

        return entries;
    }

    public void deleteEntry(String destination, String subnet, String nextHop) {
        for (int i = 0; i < table.size(); i++) {
            Entry entry = (Entry) table.get(i);

            if (entry.getDestinationNetwork().toString().equals(destination) && entry.getMask().toString().equals(subnet) && entry.getNextHopAddress().toString().equals(nextHop)) {
                table.remove(i);
                i--;
            }
        }
    }

    public boolean entryExists(String destination, String subnet, String nextHop) {
        boolean entryExists = false;

        for (int i = 0; i < table.size(); i++) {
            Entry entry = (Entry) table.get(i);

            if (entry.getDestinationNetwork().toString().equals(destination) && entry.getMask().toString().equals(subnet) && entry.getNextHopAddress().toString().equals(nextHop)) {
                entryExists = true;
            }
        }

        return entryExists;
    }

    public boolean entryDirectlyConnected(String destination, String subnet) {
        boolean directlyConnected = false;

        for (int i = 0; i < table.size(); i++) {
            Entry entry = (Entry) table.get(i);

            if (entry.getDestinationNetwork().toString().equals(destination) && entry.getMask().toString().equals(subnet) && entry.getConnectionType().equals(Entry.DIRECTLY_CONNECTED)) {
                directlyConnected = true;
            }
        }

        return directlyConnected;
    }

    public void deleteEntry(Entry entry) {
        table.remove(entry);
    }

    public void deleteEntryAtRow(int row) {
        table.remove(row);
    }
}
