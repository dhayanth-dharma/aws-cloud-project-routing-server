package com.cc.client.worker.websocket.config;
import java.io.IOException;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		
		String payload = message.getPayload();
		JSONObject jsonObject = new JSONObject(payload);
		
		request_controller(jsonObject, session);
				
	}
	
	
	
	private void request_controller(JSONObject payload, WebSocketSession session) throws JsonMappingException, JsonProcessingException
	{

		try {
			MessageModel message=objectConvertion(payload);
			if(message.contrlr.contentEquals("connection") && message.function.equals("initiate_connect"))
			{
				initial_connect(session);
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
	
	public void initial_connect(WebSocketSession session)
	{
		setupInstance(session);
	}
	public void setupInstance(WebSocketSession session)
	{
		Message_Handler_Singleton messageHandler=Message_Handler_Singleton.getInstance();
		if(messageHandler.initiate(session))
		{
			ResponseMessageModel res=new ResponseMessageModel();
			res.code=201;
			res.message="Socket connected. Application on live update";
			res.status="connected";
			res.page_id=1;
			res.message_type="message";
			messageHandler.sendMsh(res);
		}
	}
	//setup request object mapping
	public MessageModel objectConvertion(JSONObject payload)
	{
		MessageModel message=new MessageModel();
		if(!payload.get("controller").equals("")||payload.get("controller").toString()!=null)
		message.contrlr=(String) payload.get("controller");
		else message.contrlr="";
		
		if(!payload.get("function").equals("")||payload.get("function").toString()!=null)
			message.function=(String) payload.get("function");
		else message.function="";
		
		if(!payload.get("purpose").equals("")||payload.get("purpose").toString()!=null)
			message.purpose=(String) payload.get("purpose");
		else message.purpose="";
		return message;
	}

}