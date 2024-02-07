package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;
import no.hvl.dat110.utils.ErrorMessages;

import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;

public class Message {

	// the up to 127 bytes of data (payload) that a message can hold
	private final byte[] data;

	// construction a Message with the data provided
	public Message(final byte[] data) {
		if (data.length >= SEGMENTSIZE) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}
		this.data = data;
	}

	public byte[] getData() {
		return this.data; 
	}

}
