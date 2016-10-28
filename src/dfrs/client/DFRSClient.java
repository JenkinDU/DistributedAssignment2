package dfrs.client;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import dfrs.DFRS;
import dfrs.DFRSHelper;

public class DFRSClient {
	static DFRS dfrsImpl;

	public static void main(String args[]) {
		initClient(args);
	}

	private static void initClient(String[] args) {
		try {
			// create and initialize the ORB
			Properties props = new Properties();
        	props.put("org.omg.CORBA.ORBInitialPort", "1051");
        	props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			ORB orb = ORB.init(args, props);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext,
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "DFRS";
			dfrsImpl = DFRSHelper.narrow(ncRef.resolve_str(name));

			System.out.println("Connect to server success!");
			System.out.println(dfrsImpl.getBookedFlightCount("CORB SAY"));

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}
}
