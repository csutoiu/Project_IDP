package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class Client implements Runnable{
	
	private SocketChannel clientChannel;
	private Selector selector;
	private String message;
	private static Logger logger = Logger.getLogger(Client.class);
	
	private String ip;
	private int port;
	
	public Client(String ip, String port, String message) {
		this.port = Integer.parseInt(port);
		this.ip = ip;
		this.message = message;
	}
	
	@Override
	public void run() {
		boolean done = false;

		try {
			selector = Selector.open();
			
			clientChannel = SocketChannel.open();
		    clientChannel.configureBlocking(false);
		  
		    // Kick off connection establishment
		    clientChannel.connect(new InetSocketAddress(this.ip, this.port));
		    clientChannel.register(selector, SelectionKey.OP_CONNECT);
		    
			while(!done) {
				selector.select();
			
				Iterator<SelectionKey> skeys = selector.selectedKeys().iterator();
			
				while(skeys.hasNext()) {
					SelectionKey key = (SelectionKey)skeys.next();
					skeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isConnectable()) {
						finishConnection(key);
					} else if (key.isReadable()) {
						//read(key);
					} else if (key.isWritable()) {
						write(key);
						done = true;
					}
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
	}

	private void finishConnection(SelectionKey key) throws IOException {
		clientChannel.finishConnect();
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	private void write(SelectionKey key) throws IOException {
		String toWrite = this.message;
		logger.debug("Send message: " + toWrite);
		 
		if(toWrite != null) {
			ByteBuffer b;
			b = ByteBuffer.wrap(toWrite.getBytes());
			
			// b.flip();
			
			while(true) {
				int n = clientChannel.write(b);
				if(n == 0 || b.remaining() == 0)
					break ;
			}	
		}
	}
}
