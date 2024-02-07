package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static int MESSAGINGPORT = 8083;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {
		byte[] data = message.getData();
		if (data == null) {
			return null;
		}

		int dataLength = MessageUtils.getSegmentSize(data);

		if (dataLength >= SEGMENTSIZE || dataLength < 0) {
			throw new UnsupportedOperationException("Data is outside of bounds");
		}
		byte[] segment = new byte[SEGMENTSIZE];
		segment[0] = (byte)dataLength;
		arraycopy(data, 0, segment, 1, dataLength);
		return segment;

	}

	public static Message decapsulate(byte[] segment) {
		if (segment == null || segment.length == 0) {
			return null;
		}
		int segmentLength = segment[0];
		byte[] data = new byte[segmentLength];
		arraycopy(segment, 1, data, 0, segmentLength);

		return new Message(data);
	}

	public static int getSegmentSize(final byte[] bytes) {
		int index = Arrays.asList(bytes).indexOf(null);
		return index != -1 ? index : bytes.length;
	}
}
