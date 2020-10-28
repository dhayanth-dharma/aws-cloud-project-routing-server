package com.cc.client.worker.websocket.config;
import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message_Handler_Singleton {
	private static Message_Handler_Singleton obj=null;
	private Object session;
	private void Message_Handler_Singleton(){
	}
	public static Message_Handler_Singleton getInstance(){ 
        if (obj == null) 
        	obj = new Message_Handler_Singleton(); 
        return obj; 
    }
	public boolean initiate(Object object){
		try {
			this.session=object;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public WebSocketSession getSession(){
		return (WebSocketSession) session;
	}
	public void sendMsh(Object message){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
		    String jsonString;		
			jsonString = objectMapper.writeValueAsString(message);
			WebSocketSession lsession=getSession();
			lsession.sendMessage(new TextMessage(jsonString));
		
		} catch (IOException e) {
	    		e.printStackTrace();
				System.out.println(e.getMessage());
		}      
	}
}
