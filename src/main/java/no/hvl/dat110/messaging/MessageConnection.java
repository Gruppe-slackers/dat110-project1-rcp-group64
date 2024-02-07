package no.hvl.dat110.messaging;


import no.hvl.dat110.TODO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Semaphore;


public class MessageConnection {

	private DataOutputStream outStream; // for writing bytes to the underlying TCP connection
	private DataInputStream inStream; // for reading bytes from the underlying TCP connection
	private Socket socket; // socket for the underlying TCP connection
	public final Semaphore reqQueue = new Semaphore(1);
	public final Semaphore resQueue = new Semaphore(1);

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
			throw new UnsupportedOperationException("Message is not valid");
		}
		byte[] data = MessageUtils.encapsulate(message);
		if (data == null) {
			throw new UnsupportedOperationException("missing data submitted");
		}

		System.out.println("sent: " + Arrays.toString(data));
		outStream.write(data);
	}

	public Message receive() throws IOException {
		int receivedLength = inStream.read();
		if (receivedLength == -1) {
			return null;
		}
		byte[] receivedMessage = new byte[receivedLength];
		if (receivedLength != 0) {
			receivedMessage = inStream.readNBytes(receivedLength);
		}

		if (receivedMessage == null || receivedMessage.length > MessageUtils.SEGMENTSIZE) {
			throw new UnsupportedOperationException("Mottatt data er ikke gyldig");
		}

		return new Message(receivedMessage);
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