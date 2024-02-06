package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;

import java.math.BigInteger;

import static java.lang.System.arraycopy;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;

public class RPCUtils {
	
	public static byte[] encapsulate(byte rpcid, byte[] payload) {
		int segmentSize = getSegmentSize(payload);
		if (segmentSize > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}

		byte[] rpcmsg = new byte[segmentSize + 1];
		rpcmsg[0] = rpcid;
		arraycopy(payload, 0, rpcmsg, 1, segmentSize);
		
		return rpcmsg;
	}
	
	public static byte[] decapsulate(byte[] rpcmsg) {

		int segmentSize = getSegmentSize(rpcmsg);
		if (segmentSize > SEGMENTSIZE) {
			throw new UnsupportedOperationException(TODO.method());
		}

		byte[] payload = new byte[segmentSize -1];
		arraycopy(rpcmsg, 0, payload, 0, segmentSize);
		
		return payload;
		
	}

	// convert String to byte array
	public static byte[] marshallString(String str) {
		
		byte[] encoded = str.getBytes();
		if (encoded.length > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return encoded;
	}

	// convert byte array to a String
	public static String unmarshallString(byte[] data) {
		if (getSegmentSize(data) > SEGMENTSIZE)
			throw new UnsupportedOperationException(TODO.method());


		return new String(data);
	}
	
	public static byte[] marshallVoid() {
		return new byte[0];
	}
	
	public static void unmarshallVoid(byte[] data) {
		if (data.length >= 1) {
			throw new UnsupportedOperationException(TODO.method());
		}

	}

	// convert boolean to a byte array representation
	public static byte[] marshallBoolean(boolean b) {
		
		byte[] encoded = new byte[1];
				
		if (b) {
			encoded[0] = 1;
		} else
		{
			encoded[0] = 0;
		}
		
		return encoded;
	}

	// convert byte array to a boolean representation
	public static boolean unmarshallBoolean(byte[] data) {
		
		return (data[0] > 0);
		
	}

	// integer to byte array representation
	public static byte[] marshallInteger(int x) {

		byte[] encoded = BigInteger.valueOf(x).toByteArray();

		if (getSegmentSize(encoded) > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return encoded;
	}
	
	// byte array representation to integer
	public static int unmarshallInteger(byte[] data) {

		if (getSegmentSize(data) > SEGMENTSIZE-1) {
			throw new UnsupportedOperationException(TODO.method());
		}

		return new BigInteger(data).intValue();
		
	}
}
