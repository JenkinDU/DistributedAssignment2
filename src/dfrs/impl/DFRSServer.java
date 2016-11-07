package dfrs.impl;
// A server for the Hello object

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import dfrs.DFRS;
import dfrs.DFRSHelper;
import dfrs.database.FlightData;
import dfrs.database.TicketData;
import dfrs.utils.Log;

public class DFRSServer {

	public static void main(String args[]) {
		// initServer(args);
	}

	public void initServer(String args[], Properties props, String server, String name, int udp, int tudp) {
		try {
			String s = "[" + server + "]-" + "DFRSServer ready and waiting ...";
			System.out.println(s);
			FlightData.getInstance().initData(server);
			TicketData.getInstance().initData(server);
			// create and initialize the ORB
			ORB orb = ORB.init(args, props);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = (POA) orb.resolve_initial_references("RootPOA");
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			DFRSImpl helloImpl = new DFRSImpl(server, name, udp, tudp);
			helloImpl.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
			// and cast the reference to a CORBA reference
			DFRS href = DFRSHelper.narrow(ref);

			// get the root naming context
			// NameService invokes the transient name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt, which is part of the
			// Interoperable Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String n = "DFRS";
			NameComponent path[] = ncRef.to_name(n);
			ncRef.rebind(path, href);

			Log.createLogDir(Log.LOG_DIR + "LOG_" + server + "/");

			Log.i(Log.LOG_DIR + "LOG_" + server + "/" + server + "_LOG.txt", s);

			// wait for invocations from clients
			orb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
		System.out.println("DFRSServer Exiting ...");
	}
}
