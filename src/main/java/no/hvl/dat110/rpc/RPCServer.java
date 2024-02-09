package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingServer;
import no.hvl.dat110.utils.ErrorMessages;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;

public class RPCServer implements Runnable {

	private final MessagingServer msgserver;
	private MessageConnection connection;

	// hashmap to register RPC methods which are required to extend RPCRemoteImpl
	// the key in the hashmap is the RPC identifier of the method
	private final HashMap<Byte,RPCRemoteImpl> services;

	public RPCServer(int port) {
		this.msgserver = new MessagingServer(port);
		this.services = new HashMap<>();
	}

	public void run() {

		// the stop RPC method is built into the server
		RPCRemoteImpl rpcstop = new RPCServerStopImpl(RPCCommon.RPIDSTOP,this);

		System.out.println("RPC SERVER RUN - Services: " + services.size());

		connection = msgserver.accept();
		System.out.println("RPC SERVER ACCEPTED");

		boolean stop = false;

		byte rpcid;
		Message requestmsg;
		Message replymsg;

		while (!stop) {
			rpcid = 0;
			replymsg = null;
			requestmsg = this.connection.receive();

			byte[] request = requestmsg.getData();
			try {
				rpcid = request[0];
			} catch (Exception ignored) {}

			if (!services.containsKey(rpcid)) {
				throw new UnsupportedOperationException(ErrorMessages.missingRPCID());
			}

			if (request.length > 0) {
				byte[] requestBytes = RPCUtils.decapsulate(requestmsg.getData());
				byte[] responseBytes = services.get(rpcid).invoke(requestBytes);

				replymsg = new Message(RPCUtils.encapsulate(rpcid, responseBytes));
			}

			if (replymsg != null && replymsg.getData().length > SEGMENTSIZE) {
				throw new UnsupportedOperationException(ErrorMessages.maxLimit());
			}


			if (rpcid == RPCCommon.RPIDSTOP) {
				System.out.println("STOPPING SERVER");
				stop = true;
			} else {
				this.connection.send(replymsg);
			}
		}

	}

	// used by server side method implementations to register themselves in the RPC server
	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}

	public void stop() {

		if (this.connection != null) {
			this.connection.close();
		} else {
			System.out.println("RPCServer.stop - connection was null");
		}

		if (this.msgserver != null) {
			this.msgserver.stop();
		} else {
			System.out.println("RPCServer.stop - msgserver was null");
		}

	}
}
