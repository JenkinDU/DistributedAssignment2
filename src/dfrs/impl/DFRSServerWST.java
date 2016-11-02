package dfrs.impl;

import java.util.Properties;

public class DFRSServerWST extends DFRSServer {
	public static final String SERVER_NAME = "WST";
	public static final String NAME = "Washington";
	public static final String PORT_NUM = "1051";
	public static final int UDP_PORT_NUM = 3021;
	public static final int T_UDP_PORT_NUM = 4021;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_NUM);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DFRSServerWST()).initServer(args, props, SERVER_NAME, NAME, UDP_PORT_NUM, T_UDP_PORT_NUM);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
