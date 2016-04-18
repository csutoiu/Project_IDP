package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Client implements Runnable{
	
	private SocketChannel clientChannel;
	private Selector selector;
	private ByteBuffer readBuf = ByteBuffer.allocate(8192);
	private String message;
	
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
			System.out.println(e);
		}
	}

	private void finishConnection(SelectionKey key) throws IOException {
		System.out.println("Finish connection");
		clientChannel.finishConnect();
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	private void write(SelectionKey key) throws IOException {
		String toWrite = this.message;
		System.out.println("writing :" + toWrite);
		
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
		
		//key.interestOps(SelectionKey.OP_READ) ;
	}
	
	/*public void read(SelectionKey key) throws IOException {
		
		readBuf.clear() ;
		
		while (true) {
            // readBuffer.clear();

            int numread = clientChannel.read( readBuf );

            if (numread <=0) {
              break;
            }
            
           
          }
		
		   System.out.println("Read Echo from server:" + new String(readBuf.array())) ;
		   
		
		
		key.interestOps(SelectionKey.OP_WRITE) ;
		
	}*/
	
}
