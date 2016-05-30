package com.myseriousorganization.application.java.nio;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by asfernando on 5/28/16.
 */
public class FileNIOAdapterTest {

	@Test
	public void test() {
		File file = new File(this.getClass().getClassLoader().getResource("file-nio-adapter/nio-file-test.txt").getFile());

		try ( FileNIOAdapter adapter = new FileNIOAdapter(file) ) {
			String content = adapter.getContent();
			Assert.assertEquals("This is a test file.", content);
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage(), false);
		}
	}
}
