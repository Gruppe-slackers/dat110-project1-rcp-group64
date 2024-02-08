package no.hvl.dat110.rpc.tests;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCLocalStub;
import no.hvl.dat110.rpc.RPCUtils;

import java.io.IOException;
import java.util.Arrays;

public class TestBooleanBooleanStub extends RPCLocalStub {
	
	public TestBooleanBooleanStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public boolean m(boolean b) {
		
		byte[] request = RPCUtils.marshallBoolean(b);

		
		byte[] reply = new byte[127];
		try{
			reply = rpcclient.call((byte)4,request);

		}catch (IOException e){
			e.printStackTrace();
		}

		boolean bres = RPCUtils.unmarshallBoolean(reply);
		
		return bres;
	}
	
}
