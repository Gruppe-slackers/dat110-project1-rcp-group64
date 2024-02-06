package no.hvl.dat110.system.controller;

import no.hvl.dat110.TODO;
import no.hvl.dat110.rpc.*;
import java.io.IOException;

public class DisplayStub extends RPCLocalStub {

	public DisplayStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public void write (String message) {

		byte[] request = RPCUtils.marshallString(message);
		byte[] response = new byte[127];

		try{
			response = rpcclient.call((byte)Common.WRITE_RPCID, request);
		}catch (IOException e){
			System.err.println("error: " + e);
			e.printStackTrace();
		}
		String reply = RPCUtils.unmarshallString(response);


	}
}
