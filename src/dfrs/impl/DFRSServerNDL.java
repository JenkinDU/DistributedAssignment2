package dfrs.impl;

import java.util.Properties;

public class DFRSServerNDL extends DFRSServer {
	public static final String SERVER_NAME = "NDL";
	public static final String PORT_NUM = "1052";
	public static final int UDP_PORT_NUM = 3022;
	
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", PORT_NUM);
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			(new DFRSServerNDL()).initServer(args, props, SERVER_NAME, "New Delhi", UDP_PORT_NUM);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
