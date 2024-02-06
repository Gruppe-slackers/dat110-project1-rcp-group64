package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.*;

import java.io.IOException;

public class RPCClient {

	// underlying messaging client used for RPC communication
	private MessagingClient msgclient;

	// underlying messaging connection used for RPC communication
	private MessageConnection connection;
	
	public RPCClient(String server, int port) {
	
		msgclient = new MessagingClient(server,port);
	}
	
	public void connect() {
		
		// connect using the RPC client

		try{
			connection = msgclient.connect();
		}catch (IOException e){
			System.err.println("connection failed: " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public void disconnect() {
		
		// disconnect by closing the underlying messaging connection

		if(connection == null) return;
		
		connection.close();
	}

	/*
	 Make a remote call om the method on the RPC server by sending an RPC request message and receive an RPC reply message
	 rpcid is the identifier on the server side of the method to be called
	 param is the marshalled parameter of the method to be called
	 */

	public byte[] call(byte rpcid, byte[] param) {

		/*
		The rpcid and param must be encapsulated according to the RPC message format
		The return value from the RPC call must be decapsulated according to the RPC message format
		*/
		
		byte[] returnval = null;

		byte[] request = RPCUtils.encapsulate(rpcid, param);
		returnval = RPCUtils.decapsulate(request);


				

		
		return returnval;
		
	}

}
