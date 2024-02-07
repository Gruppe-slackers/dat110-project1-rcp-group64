package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingClient;

import java.io.IOException;

import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;
import static no.hvl.dat110.rpc.RPCUtils.MARSHALSIZE;
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
		this.connection.close();

		boolean closed;
		try {
			this.connection.receive();
			closed = false;
		}catch (Exception e) {
			closed = true;
		}

		if (!closed) {
			throw new RuntimeException("Connection not closed");
		}
	}

	/*
	 Make a remote call om the method on the RPC server by sending an RPC request message and receive an RPC reply message

	 rpcid is the identifier on the server side of the method to be called
	 param is the marshalled parameter of the method to be called
	 */

	public byte[] call(final byte rpcid, final byte[] param) throws IOException {
		if (getSegmentSize(param) > MARSHALSIZE-1) {
			throw new UnsupportedOperationException();
		}

		Message message = new Message(RPCUtils.encapsulate(rpcid, param));
		System.out.println(message);
		final MessageConnection messageConnection = this.connection;
		synchronized (messageConnection) {
			try {
				byte[] responseData = null;

				messageConnection.reqQueue.acquire();
				messageConnection.send(message);
				Message response = messageConnection.receive();
				while (response == null) {
					response = messageConnection.receive();

					/** making sure we get response matching our rpcid */
					if (response != null) {
						responseData = response.getData();
						if (responseData != null && (int) responseData[0] != rpcid) {
							response = null;
						}
					}
				}

				/** release and notify other clients to ask for request */
				messageConnection.reqQueue.release();
				messageConnection.notify();
				if (responseData == null) {
					return null;
				}
				return RPCUtils.decapsulate(responseData);

			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
