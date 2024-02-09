package no.hvl.dat110.messaging;


import no.hvl.dat110.utils.ErrorMessages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.arraycopy;
import static java.lang.System.getLogger;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.decapsulate;


public class MessageConnection {

	private DataOutputStream outStream; // for writing bytes to the underlying TCP connection
	private DataInputStream inStream; // for reading bytes from the underlying TCP connection
	private Socket socket; // socket for the underlying TCP connection
	public final AtomicBoolean trigger = new AtomicBoolean(true);
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

	public void send(Message message) {
		if (message ==  null) {
			return;
		}
		if (message.getData().length > SEGMENTSIZE) {
			throw new UnsupportedOperationException("Message is not valid");
		}
		byte[] data = MessageUtils.encapsulate(message);
		if (data == null) {
			throw new UnsupportedOperationException("missing data submitted");
		}

        try {
            outStream.write(data);
        } catch (IOException ignored) {}
    }

	public Message receive() {
		byte[] receivedMessage = new byte[SEGMENTSIZE];

		try {
			inStream.readFully(receivedMessage);
		} catch (IOException e) {
			System.out.println(e);
		}

		return decapsulate(receivedMessage);
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