package dfrs.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferReservation {
	private static TransferReservation instance;
	private HashMap<String, List<ITransaction>> data;
	
	private TransferReservation() {
		data = new HashMap<String, List<ITransaction>>();
	}

	public static synchronized TransferReservation getInstance() {
		if (instance == null) {
			instance = new TransferReservation();
		}
		return instance;
	}
	
	public synchronized boolean initTransaction(String id, ITransaction t) {
		if(data.containsKey(id)) {
			return false;
		} else {
			ArrayList<ITransaction> list = new ArrayList<ITransaction>();
			list.add(t);
			data.put(id, list);
			return true;
		}
	}
	
	public boolean addTransactionOperation(String id, ITransaction t) {
		if(data.containsKey(id)) {
			List<ITransaction> list = data.get(id);
			list.add(t);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean doTransaction(String id) {
		boolean result = false;
		List<ITransaction> list = data.get(id);
		if(list == null) {
			return result;
		}
		try {
			for(ITransaction l:list) {
				l.doCommit();
			}
			result = true;
		} catch(Exception e) {
			for(ITransaction l:list) {
				l.backCommit();
			}
			result = false;
		} finally {
			data.remove(id);
		}
		return result;
	}
	
	public void removeTransaction(String id) {
		data.remove(id);
	}
}
