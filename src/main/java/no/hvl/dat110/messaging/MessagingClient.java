package no.hvl.dat110.messaging;


import no.hvl.dat110.TODO;

import java.io.IOException;
import java.net.Socket;

public class MessagingClient {

	// name/IP address of the messaging server
	private final String server;

	// server port on which the messaging server is listening
	private final int port;
	
	public MessagingClient(final String server, final int port) {
		this.server = server;
		this.port = port;
	}
	
	// setup of a messaging connection to a messaging server
	public MessageConnection connect () {

		// client-side socket for underlying TCP connection to messaging server
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(this.server, this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MessageConnection connection;

		if (clientSocket == null) {
			throw new UnsupportedOperationException(TODO.method());
		} else {
			connection = new MessageConnection(clientSocket);
		}

		return connection;
	}


}
