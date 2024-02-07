package no.hvl.dat110.utils;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;

import java.util.Arrays;

import static java.lang.System.arraycopy;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;

public class Encapsulate {
    public static byte[] encapsulate(byte[] head, byte[] data) {
        if (data.length + head.length > SEGMENTSIZE) {
            throw new UnsupportedOperationException("data received is too long");
        }

        byte[] response = new byte[SEGMENTSIZE];
        arraycopy(head, 0, response, 0, head.length);
        arraycopy(data, 0, response, head.length, data.length);
        return response;
    }

    public static byte[] decapsulate(int headSize, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        int segmentSize = data[0];
        if (segmentSize != data.length - headSize) {
            throw new UnsupportedOperationException(TODO.method());
        }
        byte[] response = new byte[segmentSize];
        arraycopy(data, headSize, response, 0, segmentSize);
        return response;
    }
}
