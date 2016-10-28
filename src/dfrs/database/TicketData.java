package dfrs.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dfrs.bean.Ticket;

public class TicketData {
	private static TicketData instance;
	private HashMap<String, HashMap<String, List<Ticket>>> data;
	private int recordID = 0;
	
	private TicketData() {
		data = new HashMap<String, HashMap<String, List<Ticket>>>();
	}

	public static synchronized TicketData getInstance() {
		if (instance == null) {
			instance = new TicketData();
		}
		return instance;
	}

	public synchronized HashMap<String, List<Ticket>> initData(String name) {
		HashMap<String, List<Ticket>> o = data.get(name);
		if (o == null) {
			o = new HashMap<String, List<Ticket>>();
			data.put(name, o);
		}
		return data.get(name);
	}
	

	public synchronized void addTicket(String name, Ticket t, String index) {
		HashMap<String, List<Ticket>> o = data.get(name);
		ArrayList<Ticket> list = (ArrayList<Ticket>) o.get(index);
		if(list == null)
			list = new ArrayList<Ticket>();
		t.setRecordID(++recordID);
		list.add(t);
		o.put(index, list);
	}
}
