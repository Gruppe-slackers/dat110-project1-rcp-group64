package no.hvl.dat110.messaging;
import no.hvl.dat110.TODO;
import java.util.Arrays;
import static java.lang.System.arraycopy;
import static java.lang.System.in;

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
		int dataLength = Arrays.asList(data).indexOf(null) > 0 ? Arrays.asList(data).indexOf(null) : data.length;
		if (dataLength > 127) {
			throw new UnsupportedOperationException(TODO.method());
		}

		segment[0] = (byte)dataLength;
		arraycopy(segment, 1, data, 0, segment[dataLength]);

		return segment;
		
	}

	public static Message decapsulate(byte[] segment) {

		Message message = null;
		
		if (segment.length > 128) {
			throw new UnsupportedOperationException(TODO.method());
		}

		// TODO - END
		
		return message;
		
	}

	public static int getSegmentSize(final byte[] bytes) {
		int index = Arrays.asList(bytes).indexOf(null);
		return index >= 0 ? index : bytes.length;
	}
	
}
