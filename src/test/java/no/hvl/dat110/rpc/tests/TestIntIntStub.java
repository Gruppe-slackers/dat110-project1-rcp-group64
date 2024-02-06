package no.hvl.dat110.rpc.tests;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCLocalStub;
import no.hvl.dat110.rpc.RPCUtils;

import java.io.IOException;

public class TestIntIntStub extends RPCLocalStub {
	
	public TestIntIntStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public int m(int x) {
				
		byte[] request = RPCUtils.marshallInteger(x);
		byte[] reply = new byte[127];
		try{
			reply = rpcclient.call((byte)3,request);
		}catch (IOException e){
			e.printStackTrace();
		}

		int xres = RPCUtils.unmarshallInteger(reply);
		
		return xres;
	}
}
