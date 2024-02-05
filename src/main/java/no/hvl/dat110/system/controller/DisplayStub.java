package no.hvl.dat110.system.controller;

import no.hvl.dat110.rpc.*;

public class DisplayStub extends RPCLocalStub {

	public DisplayStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public void write (String message) {

		// Marshall parameter to write call
		byte[] request = RPCUtils.marshallString(message);

		// Make RPC for write
		byte[] response = rpcclient.call((byte)Common.WRITE_RPCID, request);

		// Unmarshalling
		RPCUtils.unmarshallVoid(response);

	}
}
