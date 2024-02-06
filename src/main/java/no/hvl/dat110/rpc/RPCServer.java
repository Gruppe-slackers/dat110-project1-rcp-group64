package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingServer;
import java.io.IOException;
import java.util.HashMap;
import static no.hvl.dat110.messaging.MessageUtils.SEGMENTSIZE;
import static no.hvl.dat110.messaging.MessageUtils.getSegmentSize;

public class RPCServer implements Runnable {

	private MessagingServer msgserver;
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

		while (!stop) {
			try {
				requestmsg = connection.receive();
				while (requestmsg == null) {
					connection.wait();
					requestmsg = connection.receive();
				}
				connection.resQueue.release();

				if (getSegmentSize(requestmsg.getData()) > SEGMENTSIZE) {
					throw new UnsupportedOperationException(TODO.method());
				}

				rpcid = requestmsg.getData()[0];
				if (!services.containsKey(rpcid)) {
					throw new UnsupportedOperationException(TODO.method());
				}

				byte[] requestBytes = RPCUtils.decapsulate(requestmsg.getData());
				byte[] responseBytes = services.get(rpcid).invoke(requestBytes);

				replymsg = new Message(RPCUtils.encapsulate(rpcid, responseBytes));
				connection.notify();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}


            if (replymsg == null || replymsg.getData().length > SEGMENTSIZE) {
			   throw new UnsupportedOperationException(TODO.method());
		   }

			try {
				connection.send(replymsg);
			} catch (IOException e) {
                e.printStackTrace();
            }

            // stop the server if it was stop methods that was called
		   if (rpcid == RPCCommon.RPIDSTOP) {
			   rpcstop.invoke(new byte[0]);
			   stop = true;
		   }
		   connection.notify();
		   notify();
		}
	
	}
	
	// used by server side method implementations to register themselves in the RPC server
	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}
	
	public void stop() {

		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
		} else {
			System.out.println("RPCServer.stop - connection was null");
		}

		if (this.msgserver != null) {
			this.msgserver.stop();
			this.msgserver = null;
		} else {
			System.out.println("RPCServer.stop - msgserver was null");
		}

	}
}
