package no.hvl.dat110.system.display;

import no.hvl.dat110.TODO;
import no.hvl.dat110.rpc.RPCServer;
import no.hvl.dat110.system.controller.Common;
import no.hvl.dat110.system.sensor.SensorImpl;


public class DisplayDevice {
	
	public static void main(String[] args) {
		
		System.out.println("Display server starting ...");

		RPCServer dispalyserver = new RPCServer(Common.DISPLAYPORT);

		SensorImpl sensor = new SensorImpl((byte)Common.WRITE_RPCID,dispalyserver);

		dispalyserver.run();

		dispalyserver.stop();

		System.out.println("Display server stopping ...");
		
	}
}
