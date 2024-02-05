package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static int MESSAGINGPORT = 8080;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {
		
		byte[] segment = new byte[SEGMENTSIZE];
		byte[] data = message.getData();
		if (data == null) {
			return segment;
		}

		int dataLength = MessageUtils.getSegmentSize(data) -1;

		if (dataLength > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}
		segment[0] = (byte)dataLength;
		arraycopy(segment, 1, data, 0, dataLength);

		return segment;

	}

	public static Message decapsulate(byte[] segment) {

		int segmentLength = getSegmentSize(segment);
		if(segment == null || segment.length > SEGMENTSIZE) {
			throw new UnsupportedOperationException(TODO.method());
		}
		byte[] data = new byte[segmentLength];
		arraycopy(data, 0, segment, 1, segmentLength -1);

		return new Message(data);
	}

	public static int getSegmentSize(final byte[] bytes) {
		int index = Arrays.asList(bytes).indexOf(null);
		return index >= 0 ? index : bytes.length;
	}
}
