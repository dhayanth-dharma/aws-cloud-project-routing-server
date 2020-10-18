package com.cc.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.Jackson;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;

import com.cc.model.NumberListRequest;
import com.cc.services.SqsService;
import com.cc.websocket.config.* ;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Equivalence.Wrapper;
@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class})

@RestController
@RequestMapping(value = "/api/sqs")

@CrossOrigin
public class SqsController {
	@Autowired
    private QueueMessagingTemplate queueMessagingTemplate;
    @Autowired
    private SqsService sqsService;
    private Message_Handler_Singleton messagePip;
	
	// Name of the queue. Developers are free to choose their queue name.
    private static final String QUEUE = "test";
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);
    @Value("${aws.endpoint.number_list_sender}")
    private String endpoint; 
//    @Value("${aws.queue.number_list_reciever}")
    private static final String reciever_queue_num_list="sqs_number_list_reciever_poll";
//    @Value("${aws.queue.number_list_sender}")
    private final String sender_queue_num_list="sqs_number_list_sender_poll";	
    
  
    @CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path= "/send/{message}" )
    public ResponseEntity<String> sendMessageQueue(@PathVariable String message) {
        LOGGER.info("Sending the message to the Amazon sqs.");
        List<String> intList=new ArrayList();
    	intList.add("23");
    	intList.add("534");
    	String messageBody=Jackson.toJsonString(intList);   	
    	queueMessagingTemplate.convertAndSend(QUEUE, messageBody);
        LOGGER.info("Message sent successfully to the Amazon sqs.");
         return new ResponseEntity("Message sent successfully to the Amazon sqs.", HttpStatus.OK); 
    }
   
    @CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path= "/num_l" )
    public ResponseEntity<String> sendNumberList(@RequestBody final NumberListRequest request) {
        
    	LOGGER.info("Sending the message to the Amazon sqs.");
        
        if(!request.input.isEmpty()){
        	String messageBody=Jackson.toJsonString(request);
        	System.out.println("messageBodyIs:>>> "+messageBody);
        	queueMessagingTemplate.convertAndSend(sender_queue_num_list, messageBody);
            LOGGER.info("Message sent successfully to the Amazon sqs.");
            return new ResponseEntity("Message sent successfully to the Amazon sqs.", HttpStatus.OK); 
        }else {
        	return new ResponseEntity<>("Number List Empty", HttpStatus.BAD_REQUEST);
        }
    	
    }
    
    @SqsListener(value = sender_queue_num_list, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	  public void getMessageFromSqs(String message, 
			  @Header("MessageId") String messageId,
			  @Header("ApproximateFirstReceiveTimestamp") String approximateFirstReceiveTimestamp
			  ) {
		LOGGER.info("Received message= {}", message);
		System.out.print(messageId+"<<"+approximateFirstReceiveTimestamp);
		ObjectMapper mapper = new ObjectMapper();
	
//		List<Integer> numList=new ArrayList<>();
		NumberListRequest request=new NumberListRequest();
		try {
//			numList = mapper.readValue(message, TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class));
			request = mapper.readValue(message, NumberListRequest.class);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final double average=sqsService.calculateMedian(request);
		request.output=average;
		ResponseMessageModel msg=new ResponseMessageModel();
		msg.message=Jackson.toJsonString(request);
		msg.page_id=2;
		msg.func_id=200;
		msg.message_type="message";
		messagePip=Message_Handler_Singleton.getInstance();
		messagePip.sendMsh(msg);
		LOGGER.info("Successfully Dispatched");
	 }
    
//    
//    @SqsListener("javatechie-queue")
//    public void loadMessageFromSQS(String message)  {
//    	LOGGER.info("message from SQS Queue {}",message);
//    }
    
    
    
//    @PostMapping(value = "/send")
//    @ResponseStatus(code = HttpStatus.CREATED)
//    public void sendMessageToSqs(@RequestBody final Message message) {
//    	
//        LOGGER.info("Sending the message to the Amazon sqs.");
//        List<String> intList=new ArrayList();
//    	intList.add("23");
//    	intList.add("534");
//        queueMessagingTemplate.convertAndSend(QUEUE, intList);
//        LOGGER.info("Message sent successfully to the Amazon sqs.");
//    }
 
//    @SqsListener(value = QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    public void getMessageFromSqs( Message message, @Header("MessageId") String messageId) {
//        LOGGER.info("Received message= {} with messageId= {}", message, messageId);
//        // TODO - Developer can do some operations like saving the message to the database, calling any 3rd party etc.
//    }
	  
	  
	  
	  
}
