package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;
import org.apache.maven.surefire.shared.lang3.ArrayUtils;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static int MESSAGINGPORT = 8080;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {
		
		byte[] segment = new byte[128];
		byte[] data = message.getData();
		if (data == null) {
			return segment;
		}

		// encapulate/encode the payload data of the message and form a segment
		// according to the segment format for the messaging layer
		segment = ArrayUtils.addFirst(segment, (byte)data.length);
		segment = ArrayUtils.addAll(segment, data);

		if (segment.length > 128) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return segment;
		
	}

	public static Message decapsulate(byte[] segment) {

		byte[] data = segment;
		data = ArrayUtils.remove(data, 0);

		Message message = new Message(data);

		if (message.equals(SEGMENTSIZE))
			throw new UnsupportedOperationException(TODO.method());
		
		return message;
		
	}
	
}
