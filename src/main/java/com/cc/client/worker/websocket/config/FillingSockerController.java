package com.cc.client.worker.websocket.config;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FillingSockerController  extends TextWebSocketHandler{
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
		request_control(jsonObject, session);
	}
	
	private void request_control(JSONObject payload, WebSocketSession session) throws JsonMappingException, JsonProcessingException
	{
		try {
			MessageModel message=objectConvertion(payload);
			if(message.function=="someFun")
			session.sendMessage(new TextMessage("Hi  how may we help you?"));
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
	}
	
	public MessageModel objectConvertion(JSONObject payload)
	{
		MessageModel message=new MessageModel();
		if(!payload.get("function").equals("")||payload.get("function").toString()!=null)
		message.function=(String) payload.get("function");
		return message;
	}
}
