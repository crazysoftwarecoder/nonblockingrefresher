package com.myseriousorganization.application.java.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by asfernando on 5/27/16.
 */
public class FileNIOAdapter implements AutoCloseable {

	private final RandomAccessFile file;

	private final FileChannel channel;

	public FileNIOAdapter(File file) throws FileNotFoundException {
		this.file = new RandomAccessFile(file, "rw");
		this.channel = this.file.getChannel();
	}

	public String getContent() throws IOException {

		StringBuilder content = new StringBuilder();

		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = this.channel.read(buf);
		while (bytesRead != -1) {

			buf.flip();

			while(buf.hasRemaining()){
				content.append((char) buf.get());
			}

			buf.clear();
			bytesRead = this.channel.read(buf);
		}

		return content.toString();
	}

	@Override
	public void close() throws Exception {
		this.file.close();
	}
}
