package com.myseriousorganization.application.java.nio.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by asfernando on 5/29/16.
 */
public class NIOServerPilotTest {

	@Test
	public void test() {
		try {
			NIOServerPilot server = new NIOServerPilot(8080);
			Thread thread = new Thread(server);
			thread.start();
			thread.join();
		}
		catch (Exception e) {
			Assert.assertTrue(e.getMessage(), false);
		}
	}
}
