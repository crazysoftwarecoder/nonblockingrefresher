package com.myseriousorganization.application.java.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * Created by asfernando on 5/28/16.
 */
public class NIOServerPilot implements Runnable {

	private final int port;

	private final ServerSocketChannel channel;

	private final Selector generalSelector;

	private ByteBuffer readBuffer;

	private ByteBuffer writeBuffer;

	public NIOServerPilot(int port) throws IOException {
		this.port = port;
		this.channel = ServerSocketChannel.open();
		this.channel.socket().bind(new InetSocketAddress(port));
		this.channel.configureBlocking(false);
		this.generalSelector = Selector.open();
		this.channel.register(generalSelector, SelectionKey.OP_ACCEPT);
		this.readBuffer = ByteBuffer.allocate(100);
		this.writeBuffer = ByteBuffer.allocate(100);
	}

	@Override
	public void run() {

		while (true) {
			try {
				if (generalSelector.select() > 0) {
					Set<SelectionKey> connectKeys = generalSelector.selectedKeys();
					for (SelectionKey key : connectKeys) {
						if (key.isAcceptable()) {
							SelectableChannel currentSelectableChannel = key.channel();
							if (currentSelectableChannel instanceof ServerSocketChannel) {
								ServerSocketChannel currentServerSocketChannel = (ServerSocketChannel) currentSelectableChannel;

								SocketChannel socketChannel = currentServerSocketChannel.accept();
								socketChannel.configureBlocking(false);
								socketChannel.register(generalSelector, SelectionKey.OP_READ);
							}
							// Remove the current key.
							generalSelector.selectedKeys().remove(key);
						}
						else if (key.isReadable()) {
							SelectableChannel currentSelectableChannel = key.channel();
							if (currentSelectableChannel instanceof  SocketChannel) {
								SocketChannel channel = (SocketChannel) currentSelectableChannel;

								while (channel.read(readBuffer)>0) {

								}
								readBuffer.flip();

								StringBuilder request = new StringBuilder();
								while (readBuffer.hasRemaining()) {
									request.append((char)readBuffer.get());
								}
								readBuffer.clear();

								System.out.println("REQUEST:= " + request);
								generalSelector.selectedKeys().remove(key);
								channel.register(generalSelector, SelectionKey.OP_WRITE);
							}
						}
						else if (key.isWritable()) {
							SelectableChannel currentSelectableChannel = key.channel();
							if (currentSelectableChannel instanceof  SocketChannel) {
								SocketChannel channel = (SocketChannel) currentSelectableChannel;

								writeBuffer.put("Yo woz popping dawg!".getBytes());
								writeBuffer.flip();
								channel.write(writeBuffer);
								writeBuffer.clear();

								channel.close();

								generalSelector.selectedKeys().remove(key);
							}
						}
					}
				}
			}
			catch (IOException e) {
				System.out.println("IOException occurred while fetching ACCEPT SELECTORS. Shutting down");
				System.exit(1);
			}
		}
	}
}
