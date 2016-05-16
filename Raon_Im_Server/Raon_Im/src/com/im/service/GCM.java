package com.im.service;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * class for the GCM alert function 
 * @since 	2016. 01. 23.
 * @version 1.0
 * @author 	Yoon JiSoo
 */
public class GCM {
	private String serverKey = "AIzaSyD7sXEMa1q0Y8dR9T8tjN6N8AajXitykGQ";
	private String regID;

	public GCM(String gcmID) {
		regID = gcmID;
	}

	/**
	 * method that alerts user with regID
	 * @Method	push
	 * @param	gcmMsg	message to send the user
	 */
	public void push(String type, String gcmMsg) throws Exception {
		Sender sender = new Sender(serverKey);
		
		Message message = new Message.Builder()
										.addData("title", "Im")
										.addData("type", type)
										.addData("message", gcmMsg)
										.build();

		try {
			Result result = sender.send(message, regID, 5);
			System.out.println(result.getMessageId());
		} catch(IOException e) {
			System.out.println("GCMService : [ The user is not exist ]");
			e.printStackTrace();
		}
	}
}
