package no.hvl.dat110.rpc.tests;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCLocalStub;
import no.hvl.dat110.rpc.RPCUtils;

import java.io.IOException;

public class TestVoidVoidStub extends RPCLocalStub {

	public TestVoidVoidStub (RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public void m() {
		
		byte[] request = RPCUtils.marshallVoid();
		byte[] reply = new byte[127];
		try{
			reply = rpcclient.call((byte)1,request);
		}catch (IOException e){
			e.printStackTrace();
		}
		

		RPCUtils.unmarshallVoid(reply);
		
	}
}
