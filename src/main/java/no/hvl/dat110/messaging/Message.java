package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;

import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;

public class Message {

	// the up to 127 bytes of data (payload) that a message can hold
	private final byte[] data;

	// construction a Message with the data provided
	public Message(final byte[] data) {
		if (data == null || data.length >= SEGMENTSIZE) {
			throw new UnsupportedOperationException("Message is too long");
		}
		this.data = data;
	}

	public byte[] getData() {
		return this.data; 
	}

}
