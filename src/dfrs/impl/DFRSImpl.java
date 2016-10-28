package dfrs.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.ORB;

import dfrs.DFRSPOA;
import dfrs.Result;
import dfrs.bean.Flight;
import dfrs.bean.Ticket;
import dfrs.database.FlightData;
import dfrs.database.TicketData;
import dfrs.utils.Log;
import dfrs.utils.Utils;

class DFRSImpl extends DFRSPOA {
	
	private ORB orb;
	
	private String LOG_PATH = Log.LOG_DIR+"LOG_";
	private String server;
	private String name;
	private int UDP_PORT;
	
	protected DFRSImpl(String server, String name, int udp) {
		super();
		this.server = server;
		this.name = name;
		UDP_PORT = udp;
		LOG_PATH=LOG_PATH+server+"/"+server+"_LOG.txt";
		Utils.printFlight(server);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				initServer();
			}
		}).start();
	}
	
	public void setORB(ORB orb_val) {
		orb = orb_val; 
	}
	
	@Override
	public Result transferReservation(int passengerID, String currentCity, String otherCity) {
		// TODO Auto-generated method stub
		return null;
	}

	private void initServer() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(UDP_PORT);
			// create socket at agreed port
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String receive = new String(request.getData(), 0, request.getLength()).trim();
				int count = 0;
				if (Flight.FIRST_CLASS.equals(receive)) {
					count = getRecordTypeCount(Flight.FIRST_CLASS);
				} else if (Flight.BUSINESS_CLASS.equals(receive)) {
					count = getRecordTypeCount(Flight.BUSINESS_CLASS);
				} else if (Flight.ECONOMY_CLASS.equals(receive)) {
					count = getRecordTypeCount(Flight.ECONOMY_CLASS);
				} else if (Flight.ALL_CLASS.equals(receive)) {
					count = getRecordTypeCount(Flight.ALL_CLASS);
				}
				String re = server + " " + count;
				request.setData(re.getBytes());
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
				String s = "["+server+"]-"+"Receive Require Request KEY: " + receive +" and Reply:" + re;
				System.out.print("\n"+s);
				Log.i(LOG_PATH, s);
			}
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		Log.i(LOG_PATH, "["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		Log.i(LOG_PATH, "["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	private String getCountFromOtherServers(String recordType, String ip, int port) {
		DatagramSocket aSocket = null;
		String receive = "";
		try {
			aSocket = new DatagramSocket();
			byte[] m = recordType.getBytes();
			InetAddress aHost = InetAddress.getByName(ip);
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, port);
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			receive = new String(reply.getData(), 0, reply.getLength()).trim();
//			System.out.println("\n["+server+"]-"+"Receive From Other Server: " + receive);
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		Log.e(LOG_PATH, "["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		Log.e(LOG_PATH, "["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
		return receive;
	}
	
	@Override
	public Result bookFlight(String firstName, String lastName, String address, String phone, String destination,
			String date, String ticketClass) {
		String s = "["+server+"]-"+"Request Book Flight Order Passenger Info Is\n     -FirstName:"+firstName+"\n"
				+"     -lastName:"+lastName +"\n"
				+"     -address:"+address +"\n"
				+"     -phone:"+phone +"\n"
				+"     -destination:"+destination +"\n"
				+"     -date:"+date +"\n"
				+"     -ticketClass:"+ticketClass;
		System.out.println(s);
		Log.i(LOG_PATH, s);
		ArrayList<Flight> flight = (ArrayList<Flight>)FlightData.getInstance().initData(server);
		Result result = new Result();
		boolean r = false;
		String info = "Book Success, Thank you!";
		Flight book = null;
		for(Flight f:flight) {
			if(f.getDeparture().equals(this.name)&&f.getDestination().equals(destination)&&f.getDepartureDate().equals(date)) {
				book = f;
				r = true;
				s = "     -Find Flight From "+this.name+" To "+destination+" On "+date;
				System.out.println(s);
				Log.i(LOG_PATH, s);
				break;
			}
		}
		if(r) {
			if(book!=null&book.sellTicket(ticketClass)) {
				Ticket t = new Ticket(firstName, lastName, address, phone, destination, date, ticketClass, this.name);
				String index = Character.toUpperCase(lastName.charAt(0)) + "" ;
				TicketData.getInstance().addTicket(server, t, index);
			} else {
				r = false;
				info = "Book Failed, We Didn't Have Enough "+ticketClass+" ticket.";
			}
		} else {
			info = "Book Failed, We Didn't Have This Ticket.";
		}
		s = "     -"+info;
		System.out.println(s);
		Log.i(LOG_PATH, s);
		result.success = r;
		result.content=info;
		Utils.printFlight(server);
		return result;
	}
	@Override
	public String getBookedFlightCount(String recordType) {
		String s = "["+server+"]-"+"Receive Get Booked Flight Count Request, RecordType Is: " + recordType;
		System.out.println("\n"+s);
		Log.i(LOG_PATH, s);
		int count = getRecordTypeCount(recordType);
		String value = "";
		if(DFRSServerMTL.SERVER_NAME.equals(server)) {
			value = server + " " +count+",";
			value +=getCountFromOtherServers(recordType, "localhost", DFRSServerWST.UDP_PORT_NUM);
			value +=",";
			value +=getCountFromOtherServers(recordType, "localhost", DFRSServerNDL.UDP_PORT_NUM);
		} else if(DFRSServerWST.SERVER_NAME.equals(server)) {
			value =getCountFromOtherServers(recordType, "localhost", DFRSServerMTL.UDP_PORT_NUM);
			value += ("," + server + " " +count+",");
			value +=getCountFromOtherServers(recordType, "localhost", DFRSServerNDL.UDP_PORT_NUM);
		} else if(DFRSServerNDL.SERVER_NAME.equals(server)) {
			value +=getCountFromOtherServers(recordType, "localhost", DFRSServerMTL.UDP_PORT_NUM);
			value +=",";
			value +=getCountFromOtherServers(recordType, "localhost", DFRSServerWST.UDP_PORT_NUM);
			value += ("," + server + " " +count);
		}
		s = "Reply Value Is: " + value;
		System.out.println("\n"+"["+server+"]-"+s);
		Log.i(LOG_PATH, "     -"+s);
		return value;
	}

	private int getRecordTypeCount(String recordType) {
		HashMap<String,List<Ticket>> tickets = TicketData.getInstance().initData(server);
		Iterator iter = tickets.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<Ticket> value = (ArrayList<Ticket>)entry.getValue();
			for(Ticket f:value) {
				if(f!=null) {
					if (!recordType.equals(Flight.ALL_CLASS)) {
						if (recordType.equals(f.getTicketClass())) {
							count++;
						}
					} else {
						count++;
					 }
				}
			}
		}
		return count;
	}

	@Override
	public Result editFlightRecord(int recordID, String fieldName, String newValue) {
		String s = "["+server+"]-"+"Receive Edit Flight Record Request"+" recordID:" + recordID + " fieldName:" + fieldName + " newValue:" + newValue;
		System.out.println("\n"+s);
		Log.i(LOG_PATH, s);
		ArrayList<Flight> flight = (ArrayList<Flight>)FlightData.getInstance().initData(server);
		Result result = new Result();
		boolean find = false;
		boolean r = false;
		String info = "Edit Flight Record Success, Thank you!";
		for(Flight f:flight) {
			if(f.getRecordID() == recordID) {
				s="["+server+"]-"+"Find recordID:" + f.getRecordID();
				System.out.println(s);
				Log.i(LOG_PATH, s);
				s="     -"+f.toString();
				System.out.println(s);
				Log.i(LOG_PATH, s);
				find = true;
				if(Flight.DEPARTURE.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDestination())) {
						f.setDeparture(newValue);
						r = true;
					} else {
						info = "Edit Flight Record Failed, Because Departure same with Destination.";
					}
				} else if(Flight.DATE.equals(fieldName)) {
					f.setDepartureDate(newValue);
					r = true;
				} else if(Flight.DESTINATION.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDeparture())) {
						f.setDestination(newValue);
						r = true;
					} else {
						info = "Edit Flight Record Failed, Because Destination same with Departure.";
					}
				} else if(Flight.F_SEATS.equals(fieldName)) {
					int old = f.getTotalFirstTickets()-f.getBalanceFirstTickets();//getRecordTypeCount(Flight.F_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalFirstTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = "Edit Flight Record Failed, Because new seats number less than booked number " + old;
					}
				} else if(Flight.B_SEATS.equals(fieldName)) {
					int old = f.getTotalBusinessTickets()-f.getBalanceBusinessTickets();//getRecordTypeCount(Flight.B_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalBusinessTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = "Edit Flight Record Failed, Because new seats number less than booked number" + old;
					}
				} else if(Flight.E_SEATS.equals(fieldName)) {
					int old = f.getTotalEconomyTickets()-f.getBalanceEconomyTickets();//getRecordTypeCount(Flight.E_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalEconomyTickets(Integer.valueOf(newValue));
						r = true;
					} else {
						info = "Edit Flight Record Failed, Because new seats number less than booked number" + old;
					}
				}
				if(r) {
					s = "["+server+"]-"+"Edit Record Successful New Value Is:";
					System.out.println(s);
					Log.i(LOG_PATH, s);
					s = "     -"+f.toString();
					System.out.println(s);
					Log.i(LOG_PATH, s);
					Utils.printFlight(this.server);
				}
				break;
			}
		}
		if(!find) {
			Flight f = new Flight();
			f.setRecordID(recordID);
			if(Flight.DEPARTURE.equals(fieldName)) {
				f.setDeparture(newValue);
			} else if(Flight.DATE.equals(fieldName)) {
				f.setDepartureDate(newValue);
			} else if(Flight.DESTINATION.equals(fieldName)) {
				f.setDestination(newValue);
			} else if(Flight.F_SEATS.equals(fieldName)) {
				f.setTotalFirstTickets(Integer.valueOf(newValue));
			} else if(Flight.B_SEATS.equals(fieldName)) {
				f.setTotalBusinessTickets(Integer.valueOf(newValue));
			} else if(Flight.E_SEATS.equals(fieldName)) {
				f.setTotalEconomyTickets(Integer.valueOf(newValue));
			}
			FlightData.getInstance().addNewFlight(server, f);
			r = true;
			info = "ADD Flight Record Success, Thank you!";
			s = "["+server+"]-"+"Can't Find Record Create A New One:" + f.toString();
			System.out.println(s);
			Log.i(LOG_PATH, s);
			Utils.printFlight(this.server);
		}
		result.success = r;
		result.content=info;
		
		System.out.println("     -"+info);
		Log.i(LOG_PATH, "["+server+"]-"+info);
		return result;
	}
  
}
