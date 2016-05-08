package NIO;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class Server implements Runnable{
	
	private ServerSocketChannel serverchannel;
	private Selector selector ;
	private Logger logger = Logger.getLogger(Server.class);
	
	private String ip;
	private int port;
		
	public Server(String ip, String port) {
		this.ip = ip;
		this.port = Integer.parseInt(port);
	}

	@Override
	public void run() {
		
		try {
			serverchannel = ServerSocketChannel.open();
			serverchannel.configureBlocking(false);
			serverchannel.socket().bind(new InetSocketAddress(this.ip, this.port)); 
			
			selector = Selector.open();
			serverchannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true) {
				
				selector.select();
				Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
				
				while(keysIterator.hasNext()) {
					
					SelectionKey key = keysIterator.next(); 
					keysIterator.remove();
					
					if(key.isAcceptable()) {
						ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
				        SocketChannel sc = ssc.accept();
				        sc.configureBlocking( false );
				        sc.register(selector, SelectionKey.OP_READ );
						
					} else if(key.isReadable()) {
						
						SocketChannel sc = (SocketChannel)key.channel();
						ByteBuffer readBuffer = ByteBuffer.allocate(8192);
						
						int numread;
						while(true) {
				            // readBuffer.clear();

							numread = sc.read(readBuffer);

				            if(numread <=0) {
				            	break;
				            }
				        }
						
						if (numread == -1) {
							// Remote entity shut the socket down cleanly. Do the
							// same from our end and cancel the channel.
							key.channel().close();
							key.cancel();
							continue;
						}
						
						logger.debug("Received message: " + new String(readBuffer.array()));

						try {
							MessageHandler.getReceiveEventMessage(new String(readBuffer.array()));
						} catch (Exception e) {
							logger.error(e);
						}
						
						readBuffer.flip();
						
						//queuedWrites.put(sc,readBuffer);	
						//key.interestOps(SelectionKey.OP_WRITE) ;
						
					} /*else if (key.isWritable()) {
						SocketChannel sc = (SocketChannel)key.channel();
						ByteBuffer towrite = queuedWrites.get(sc) ;
						
						System.out.println("Echoing :" + new String(towrite.array())) ;
						
						while (true) {
							int n = sc.write(towrite) ;

							if (n == 0 || towrite.remaining() == 0)
								break ;
						}	

						key.interestOps(SelectionKey.OP_READ) ;
					}*/
				}
			}
		} catch(IOException e) {
			logger.error(e);
		}
	}
}

