package no.hvl.dat110.messaging;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import no.hvl.dat110.TODO;
import org.apache.maven.surefire.shared.lang3.ArrayUtils;


public class MessageConnection {

	private DataOutputStream outStream; // for writing bytes to the underlying TCP connection
	private DataInputStream inStream; // for reading bytes from the underlying TCP connection
	private Socket socket; // socket for the underlying TCP connection
	
	public MessageConnection(Socket socket) {

		try {

			this.socket = socket;

			outStream = new DataOutputStream(socket.getOutputStream());

			inStream = new DataInputStream (socket.getInputStream());

		} catch (IOException ex) {

			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void send(Message message) throws IOException {
		if (message == null || message.getData().length > MessageUtils.SEGMENTSIZE) {
			throw new UnsupportedOperationException(TODO.method());
		}
		byte[] data = message.getData();
		outStream.write(data, 1, data[0]);

	}

	public Message receive() throws IOException {

		byte[] receivedMessage = inStream.readAllBytes();
		if (receivedMessage == null || receivedMessage.length > MessageUtils.SEGMENTSIZE) {
			throw new UnsupportedOperationException(TODO.method());
		}
		int receivedLength = receivedMessage[0];

		if (receivedLength > MessageUtils.SEGMENTSIZE-1 || receivedLength < 0) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return MessageUtils.decapsulate(receivedMessage);
		
	}

	// close the connection by closing streams and the underlying socket	
	public void close() {

		try {
			
			outStream.close();
			inStream.close();

			socket.close();
			
		} catch (IOException ex) {

			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}