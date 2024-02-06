package no.hvl.dat110.system.controller;

import no.hvl.dat110.TODO;
import no.hvl.dat110.rpc.*;

import java.io.IOException;

public class DisplayStub extends RPCLocalStub {

	public DisplayStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public void write (String message) {

		try {
			byte[] request = RPCUtils.marshallString(message);
			byte[] response = rpcclient.call((byte)Common.WRITE_RPCID, request);
			String reply = RPCUtils.unmarshallString(response);
		} catch (IOException e){
			System.out.println("Oops");
		}

	}
}
