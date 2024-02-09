package no.hvl.dat110.messaging;

import no.hvl.dat110.utils.ErrorMessages;
import java.util.Arrays;
import static java.lang.System.arraycopy;

public class MessageUtils {
	public static final int SEGMENTSIZE = 128;
	public static int MESSAGINGPORT = 8083;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {
		byte[] data = message.getData();
		if (data == null) {
			throw new UnsupportedOperationException(ErrorMessages.invalidType());
		}

		int dataLength = MessageUtils.getSegmentSize(data);
		if (dataLength >= SEGMENTSIZE || dataLength < 0) {
			throw new UnsupportedOperationException("Data is outside of bounds");
		}
		byte[] segment = new byte[SEGMENTSIZE];
		segment[0] = (byte)dataLength;
		arraycopy(data, 0, segment, 1, dataLength);
		System.out.println("MEC: " + Arrays.toString(segment));
		return segment;

	}

	public static Message decapsulate(byte[] segment) {
		if (segment == null) {
			return null;
		}

		byte[] data;
		int segmentLength = segment.length == 0 ? 0 : segment[0];
		if (segmentLength == 0) {
			data = new byte[0];
		} else {
			data = Arrays.copyOfRange(segment, 1, segmentLength + 1);
		}
		System.out.println("MDC: " + Arrays.toString(data));

		return new Message(data);
	}

	public static int getSegmentSize(final byte[] bytes) {
		int index = Arrays.asList(bytes).indexOf(null);
		return index != -1 ? index : bytes.length;
	}
}
