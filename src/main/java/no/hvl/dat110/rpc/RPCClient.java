package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingClient;
import no.hvl.dat110.utils.ErrorMessages;

import java.io.IOException;
import java.util.Arrays;

import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;
import static no.hvl.dat110.rpc.RPCUtils.MARSHALSIZE;
import static no.hvl.dat110.rpc.RPCUtils.encapsulate;

public class RPCClient extends Thread {

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
		if (this.connection != null) {
			this.connection.close();
		}
	}

	/*
	 Make a remote call om the method on the RPC server by sending an RPC request message and receive an RPC reply message

	 rpcid is the identifier on the server side of the method to be called
	 param is the marshalled parameter of the method to be called
	 */

	public byte[] call(final byte rpcid, final byte[] param) throws IOException {
		if (getSegmentSize(param) > MARSHALSIZE-1) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}

		byte[] rpcBytes = encapsulate(rpcid, param);
		Message rpcMessage = new Message(rpcBytes);

		this.connection.send(rpcMessage);

		Message response = this.connection.receive();
		if (response == null) {
			return null;
		}
		byte[] responseData = response.getData();

		if (responseData == null) {
			return null;
		}

		return RPCUtils.decapsulate(responseData);
	}


}
