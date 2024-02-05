package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.*;

import java.io.IOException;

import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
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
		if (getSegmentSize(param) > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}


		Message message = new Message(RPCUtils.encapsulate(rpcid, param));

		if (message == null) {
			throw new UnsupportedOperationException(TODO.method());
		}

		connection.send(message);
		connection.notify();
		try {
			connection.wait();
			Message response = connection.receive();
			byte[] responseData = response.getData();
			if (response == null || responseData == null) {
				return null;
			}
			connection.notify();
			return RPCUtils.decapsulate(responseData);

		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
