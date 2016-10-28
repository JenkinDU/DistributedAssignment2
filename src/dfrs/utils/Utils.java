package dfrs.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dfrs.bean.Flight;
import dfrs.database.FlightData;

public class Utils {
	public static boolean validDate(String input) {
		String pat = "\\d{8}" ;
        Pattern p = Pattern.compile(pat) ;
        Matcher m = p.matcher(input) ;
        return m.matches();
	}
	
	public static void printFlight(String server) {
//		System.out.println("["+server+"]-Flight Information:");
		List<Flight> flight = FlightData.getInstance().initData(server);
		System.out.println("ID\tDEP\t\tDES\t\tDATE\t\tF/B/E");
		for(Flight f:flight) {
			System.out.println(f.getRecordID()+"\t"+f.getDeparture()+"\t"+f.getDestination()+"\t"+f.getDepartureDate()
			+"\t"+f.getBalanceFirstTickets()+"/"+f.getBalanceBusinessTickets()+"/"+f.getBalanceEconomyTickets());
		}
	}
}
