package no.hvl.dat110.rpc;

import no.hvl.dat110.utils.ErrorMessages;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.System.arraycopy;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;

/**
 * {@link RPCUtils} used to handle RPC-REQUEST
 * Marshalling and Unmarshalling data
 * Encapsulating requests and Decapsulating response
 */
public class RPCUtils {

	/** The constants for MARSHALLING */
	public static final int MARSHALSIZE = SEGMENTSIZE - 1;

	/**
	 * Encapsulate {@link byte[]} request message.
	 * @param rpcid {@link byte} RCP-ID to assign payload too
	 * @param payload the payload of {@link byte[]}
	 * @return the byte [ ]
	 */
	public static byte[] encapsulate(byte rpcid, byte[] payload) {
		int segmentSize = getSegmentSize(payload);
		if (segmentSize > MARSHALSIZE-1) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}

		byte[] rpcmsg = new byte[segmentSize + 1];
		rpcmsg[0] = rpcid;
		arraycopy(payload, 0, rpcmsg, 1, segmentSize);

		return rpcmsg;
	}

	/**
	 * Decapsulate the request message from RPC-ID.
	 * @param rpcmsg {@link byte[]} of response message
	 * @return the {@link byte[]} of the raw message without RCP-ID
	 */
	public static byte[] decapsulate(byte[] rpcmsg) {
		if (rpcmsg == null) {
			return null;
		}
		if (rpcmsg.length == 0) {
			return new byte[0];
		}
		if (rpcmsg.length >= MARSHALSIZE) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}
		byte[] outp = Arrays.copyOfRange(rpcmsg, 1, rpcmsg.length);
		return outp;
	}

	/**
	 * Marshall {@link String} to {@link byte[]}.
	 * @param str the {@link String} we want to marshall
	 * @return the string as {@link byte[]}
	 */
// convert String to byte array
	public static byte[] marshallString(String str) {
		
		byte[] encoded = str.getBytes();
		if (encoded.length > MARSHALSIZE-1) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}

		return encoded;
	}

	/**
	 * Unmarshall {@link byte[]} to {@link String}.
	 * @param data the {@link byte[]} we want to unmarshall
	 * @return the bytes as {@link String}
	 */
// convert byte array to a String
	public static String unmarshallString(byte[] data) {
		if (getSegmentSize(data) > MARSHALSIZE)
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());

		return new String(data, StandardCharsets.UTF_8);
	}

	/**
	 * Marshall {@link Void} to {@link byte[]}.
	 * @return void as {@link byte[]}
	 */
	public static byte[] marshallVoid() {
		return new byte[0];
	}

	/**
	 * Unmarshall {@link Void}.
	 * @param data {@link byte[]} we want to unmarshall to {@link Void}
	 */
	public static void unmarshallVoid(byte[] data) {
		if (data == null || data.length == 1) {
			throw new UnsupportedOperationException(ErrorMessages.invalidType());
		}
	}

	/**
	 * Marshall {@link boolean} to {@link byte[]}.
	 * @param b the {@link boolean} to convert
	 * @return the boolean as {@link byte[]}
	 */
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

	/**
	 * Unmarshall {@link byte[]} to {@link boolean}.
	 * @param data the {@link byte[]} we want to convert
	 * @return the bytes converted to {@link boolean}
	 */
// convert byte array to a boolean representation
	public static boolean unmarshallBoolean(byte[] data) {
		return (data[0] > 0);
	}

	/**
	 * Marshall {@link int} to {@link byte[]}.
	 * @param x the integer we want to marshal to bytes
	 * @return the integer as {@link byte[]}
	 */
// integer to byte array representation
	public static byte[] marshallInteger(int x) {

		byte[] encoded = ByteBuffer.allocate(4).putInt(x).array();

		if (getSegmentSize(encoded) > MARSHALSIZE-1) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}

		return encoded;
	}

	/**
	 * Unmarshall {@link byte[]} to {@link int}.
	 * @param data the {@link byte[]} we want to unmarshall
	 * @return the bytes as {@link int}
	 */
// byte array representation to integer
	public static int unmarshallInteger(byte[] data) {

		if (getSegmentSize(data) > MARSHALSIZE-1) {
			throw new UnsupportedOperationException(ErrorMessages.maxLimit());
		}

		return ByteBuffer.wrap(data).getInt();
	}
}
