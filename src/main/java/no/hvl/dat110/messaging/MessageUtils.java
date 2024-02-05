package no.hvl.dat110.messaging;

import no.hvl.dat110.TODO;
import org.apache.maven.surefire.shared.lang3.ArrayUtils;

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

		segment = ArrayUtils.addFirst(segment, (byte)data.length);
		segment = ArrayUtils.addAll(segment, data);

		if (segment.length > 128) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return segment;
		
	}

	public static Message decapsulate(byte[] segment) {

		if(segment == null || segment.length > SEGMENTSIZE) {
			throw new UnsupportedOperationException(TODO.method());
		}
		byte[] data = segment;
		data = ArrayUtils.remove(data, 0);

		return new Message(data);
		
	}
	
}
