package dfrs.bean;

public class Flight {

	public static final String FIRST_CLASS = "First";
	public static final String BUSINESS_CLASS = "Business";
	public static final String ECONOMY_CLASS = "Economy";
	public static final String ALL_CLASS = "All";
	
	public static final String DEPARTURE = "DEPARTURE";
	public static final String DATE = "DATE";
	public static final String DESTINATION = "DESTINATION";
	public static final String F_SEATS = "First";
	public static final String B_SEATS = "Business";
	public static final String E_SEATS = "Economy";
	
	private int recordID = 0;
	
	private String flightName = "";

	private String departure = "[No Value]";
	private String departureDate = "[No Value]";
	private String destination = "[No Value]";
	private String achieveDate = "";
	
	private int totalBusinessTickets = 0;
	private int totalFirstTickets = 0;
	private int totalEconomyTickets = 0;
	private int balanceBusinessTickets = 0;
	private int balanceFirstTickets = 0;
	private int balanceEconomyTickets = 0;
	
	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getAchieveDate() {
		return achieveDate;
	}

	public void setAchieveDate(String achieveDate) {
		this.achieveDate = achieveDate;
	}

	public int getTotalBusinessTickets() {
		return totalBusinessTickets;
	}

	public void setTotalBusinessTickets(int totalBusinessTickets) {
		int add = totalBusinessTickets - this.totalBusinessTickets;
		this.totalBusinessTickets = totalBusinessTickets;
		editBalanceBusinessTickets(add);
//		this.balanceBusinessTickets += add;
	}

	public int getTotalFirstTickets() {
		return totalFirstTickets;
	}

	public void setTotalFirstTickets(int totalFirstTickets) {
		int add = totalFirstTickets - this.totalFirstTickets;
		this.totalFirstTickets = totalFirstTickets;
		editBalanceFirstTickets(add);
//		this.balanceFirstTickets += add;
	}

	public int getTotalEconomyTickets() {
		return totalEconomyTickets;
	}

	public void setTotalEconomyTickets(int totalEconomyTickets) {
		int add = totalEconomyTickets - this.totalEconomyTickets;
		this.totalEconomyTickets = totalEconomyTickets;
		editBalanceEconomyTickets(add);
//		this.balanceEconomyTickets += add;
	}
	
	public synchronized boolean sellTicket(String type, boolean sell) {
		if(!sell) {
			if (BUSINESS_CLASS.equals(type)) {
				editBalanceBusinessTickets(1);
//				balanceBusinessTickets++;
			} else if (FIRST_CLASS.equals(type)) {
				editBalanceFirstTickets(1);
//				balanceFirstTickets++;
			} else if (ECONOMY_CLASS.equals(type)) {
				editBalanceEconomyTickets(1);
//				balanceEconomyTickets++;
			}
		} else {
			if (BUSINESS_CLASS.equals(type)) {
				if (balanceBusinessTickets <= 0)
					return false;
				else
					editBalanceBusinessTickets(-1);
//					balanceBusinessTickets--;
			} else if (FIRST_CLASS.equals(type)) {
				if (balanceFirstTickets <= 0)
					return false;
				else
					editBalanceFirstTickets(-1);
//					balanceFirstTickets--;
			} else if (ECONOMY_CLASS.equals(type)) {
				if (balanceEconomyTickets <= 0)
					return false;
				else
					editBalanceEconomyTickets(-1);
//					balanceEconomyTickets--;
			}
		}
		return true;
	}
	
	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public int getBalanceBusinessTickets() {
		return balanceBusinessTickets;
	}

	private synchronized void editBalanceBusinessTickets(int v) {
		this.balanceBusinessTickets += v;
	}

	public int getBalanceFirstTickets() {
		return balanceFirstTickets;
	}

	private synchronized void editBalanceFirstTickets(int v) {
		this.balanceFirstTickets += v;
	}

	public int getBalanceEconomyTickets() {
		return balanceEconomyTickets;
	}

	private synchronized void editBalanceEconomyTickets(int v) {
		this.balanceEconomyTickets += v;
	}

	@Override
	public String toString() {
		return "Flight [recordID=" + recordID + ", departure=" + departure + ", departureDate=" + departureDate
				+ ", destination=" + destination + ", totalBusinessTickets=" + totalBusinessTickets
				+ ", totalFirstTickets=" + totalFirstTickets + ", totalEconomyTickets=" + totalEconomyTickets
				+ ", balanceBusinessTickets=" + balanceBusinessTickets + ", balanceFirstTickets=" + balanceFirstTickets
				+ ", balanceEconomyTickets=" + balanceEconomyTickets + "]";
	}
	
}
