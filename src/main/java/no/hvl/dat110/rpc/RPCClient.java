package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.*;

import java.io.IOException;

import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;

public class RPCClient {

	// underlying messaging client used for RPC communication
	private final MessagingClient msgclient;

	// underlying messaging connection used for RPC communication
	private MessageConnection connection;

	public RPCClient(final String server, final int port) {
		msgclient = new MessagingClient(server, port);
	}
	
	public void connect() {
		this.connection = msgclient.connect();
		// connect using the RPC client
		
		if (this.connection == null) {
			throw new UnsupportedOperationException(TODO.method());
		}
	}
	
	public void disconnect() {
		this.connection.close();
		this.msgclient.notify();

		boolean closed;
		try {
			this.connection.receive();
			closed = false;
		}catch (Exception e) {
			closed = true;
		}

		if (!closed) {
			throw new RuntimeException(TODO.method());
		}
	}

	/*
	 Make a remote call om the method on the RPC server by sending an RPC request message and receive an RPC reply message

	 rpcid is the identifier on the server side of the method to be called
	 param is the marshalled parameter of the method to be called
	 */

	public byte[] call(final byte rpcid, final byte[] param) throws IOException {

		byte[] returnval = null;
		// TODO - START

		if (getSegmentSize(param) > 127) {
			throw new UnsupportedOperationException(TODO.method());
		}


		connection.send(new Message(RPCUtils.encapsulate(rpcid, param)));
		/*

		The rpcid and param must be encapsulated according to the RPC message format

		The return value from the RPC call must be decapsulated according to the RPC message format

		*/
				
		if (true)
			throw new UnsupportedOperationException(TODO.method());
		
		// TODO - END
		return returnval;
		
	}

}
