package no.hvl.dat110.rpc.tests;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCLocalStub;
import no.hvl.dat110.rpc.RPCUtils;

import java.io.IOException;

public class TestStringStringStub extends RPCLocalStub {

	public TestStringStringStub(RPCClient rpcclient) {
		super(rpcclient);
	}
	
	public String m(String str) {
		
		byte[] request = RPCUtils.marshallString(str);
		byte[] reply = new byte[127];
		try{
			reply = rpcclient.call((byte)2,request);
		}catch (IOException e){
			e.printStackTrace();
		}

		String strres = RPCUtils.unmarshallString(reply);
		
		return strres;
	}
}
