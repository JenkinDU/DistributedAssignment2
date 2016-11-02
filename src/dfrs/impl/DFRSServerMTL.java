package dfrs.impl;

import java.util.Properties;

public class DFRSServerMTL extends DFRSServer {
	public static final String SERVER_NAME = "MTL";
	public static final String NAME = "Montreal";
	public static final String PORT_NUM = "1050";
	public static final int UDP_PORT_NUM = 3020;
	public static final int T_UDP_PORT_NUM = 4020;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_NUM);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DFRSServerMTL()).initServer(args, props, SERVER_NAME, NAME, UDP_PORT_NUM, T_UDP_PORT_NUM);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
