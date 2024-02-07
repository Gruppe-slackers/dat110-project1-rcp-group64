package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingServer;
import no.hvl.dat110.utils.ErrorMessages;

import java.io.IOException;
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

		byte rpcid = 0;
		Message requestmsg;
		Message replymsg = null;

		final MessageConnection messageConnection = this.connection;
		synchronized (messageConnection) {
			while (!stop) {
				try {
					requestmsg = messageConnection.receive();
					while (requestmsg == null) {
						messageConnection.wait();
						requestmsg = messageConnection.receive();
					}
					messageConnection.resQueue.release();

					if (getSegmentSize(requestmsg.getData()) > SEGMENTSIZE) {
						throw new UnsupportedOperationException(TODO.method());
					}

					byte[] request = requestmsg.getData();
					if (request.length == 0) {
						replymsg = requestmsg;
					} else {
						rpcid = request[0];
						if (!services.containsKey(rpcid)) {
							throw new UnsupportedOperationException(ErrorMessages.missingRPCID());
						}

						byte[] requestBytes = RPCUtils.decapsulate(requestmsg.getData());
						byte[] responseBytes = services.get(rpcid).invoke(requestBytes);

						replymsg = new Message(RPCUtils.encapsulate(rpcid, responseBytes));
						messageConnection.notify();
					}

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}


				if (replymsg == null || replymsg.getData().length > SEGMENTSIZE) {
					throw new UnsupportedOperationException(TODO.method());
				}

				try {
					messageConnection.send(replymsg);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// stop the server if it was stop methods that was called
				if (rpcid == RPCCommon.RPIDSTOP) {
					rpcstop.invoke(new byte[0]);
					stop = true;
				}
				messageConnection.notify();
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
