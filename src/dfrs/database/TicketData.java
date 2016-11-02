package dfrs.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dfrs.bean.Flight;
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
	

	public void addTicket(String server, Ticket t) {
		String index = Character.toUpperCase(t.getLastName().charAt(0)) + "" ;
		HashMap<String, List<Ticket>> o = data.get(server);
		ArrayList<Ticket> list = (ArrayList<Ticket>) o.get(index);
		if(list == null)
			list = new ArrayList<Ticket>();
		if(t.getRecordID() <= 0)
			t.setRecordID(++recordID);
		list.add(t);
		o.put(index, list);
	}

	public void removeTicket(String server, Ticket t) {
		String index = Character.toUpperCase(t.getLastName().charAt(0)) + "" ;
		HashMap<String, List<Ticket>> o = data.get(server);
		ArrayList<Ticket> list = (ArrayList<Ticket>) o.get(index);
		list.remove(t);
	}
	
	public Ticket getTicketRecord(String server, int id) {
		HashMap<String, List<Ticket>> o = data.get(server);
		Iterator iter = o.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<Ticket> value = (ArrayList<Ticket>) entry.getValue();
			for (Ticket f : value) {
				if (f != null) {
					if (id == f.getRecordID()) {
						return f;
					}
				}
			}
		}
		return null;
	}
}
