package com.cc.client.worker.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageVisibility;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.json.Jackson;
import com.cc.client.worker.model.LogginModel;
import com.cc.client.worker.services.LogginService;
import com.cc.client.worker.services.S3BucketService;
import com.cc.client.worker.services.SqsService;
import com.cc.client.worker.websocket.config.Message_Handler_Singleton;
import com.cc.client.worker.websocket.config.ResponseMessageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@RequestMapping(value = "/api/s3")

@CrossOrigin
public class S3Controller {
	
	@Autowired
	S3BucketService s3bucketService;
    @Value("${aws.s3.bucket.image_list.name}")
    String defaultBucketName;
    @Value("${aws.s3.bucket.image_list.original.folder}")
    String originalImgFolder;
    @Value("${aws.s3.bucket.image_list.edited.folder}")
    String editedImgFolder;
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;
    @Autowired
    private SqsService sqsService;
    @Autowired
    private LogginService logginService;
    private Message_Handler_Singleton messagePip;
    private static final String reciever_queue_image="sqs_image_reciever_poll";
    private static final String sender_queue_image="sqs_image_sender_poll";	
    public static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);
    @CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path= "/buckets" )
    public List<Bucket> listBuckets(){
        return s3bucketService.getAllBuckets();
    }
    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, path = "/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
    	try {
    		s3bucketService.uploadFile(files.getOriginalFilename(),files.getBytes());
            Map<String,String> result = new HashMap<>();
            result.put("key",files.getOriginalFilename());
            sendToSQS(files.getOriginalFilename());
            return  ResponseEntity.ok("Success");
		} catch (Exception e) {
			 return  ResponseEntity.ok(e.getMessage());
		}
    }
    @RequestMapping(method=RequestMethod.POST, path = "/uploadT",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String,String>> uploadFileWorking(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
    		s3bucketService.uploadFile(files.getOriginalFilename(),files.getBytes());
            Map<String,String> result = new HashMap<>();
            result.put("key",files.getOriginalFilename());
            sendToSQS(files.getOriginalFilename());
            return  ResponseEntity.ok(result);
    }
    public void sendToSQS(String key) {
    	String messageBody=Jackson.toJsonString(key);
    	queueMessagingTemplate.convertAndSend(sender_queue_image, messageBody);
    }
    @SqsListener(value = reciever_queue_image, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
   	public void getMessageFromSqs( String message, 
   			  @Header("MessageId") String messageId,
   			  @Header("LogicalResourceId") String logicalResourceId,
   			  @Header("ApproximateReceiveCount") String approximateReceiveCount,
   			  @Header("ApproximateFirstReceiveTimestamp") String approximateFirstReceiveTimestamp,
   			  @Header("SentTimestamp") String sentTimestamp,
   			  @Header("ReceiptHandle") String receiptHandle,
   			  @Header("Visibility") QueueMessageVisibility visibility,
   			  @Header("SenderId") String senderId,
   			  @Header("contentType") String contentType,
   			  @Header("lookupDestination") String lookupDestination
   	  ) {
			LOGGER.info("Received reciever image queue message= {}", message);
   			ObjectMapper mapper = new ObjectMapper();
			String key="";
			try {
				 key = mapper.readValue(message, String.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			ResponseMessageModel msg=new ResponseMessageModel();
   			msg.message=Jackson.toJsonString(key);
   			msg.page_id=2;
   			msg.func_id=201;
   			msg.message_type="message";
   			messagePip=Message_Handler_Singleton.getInstance();
   			messagePip.sendMsh(msg);
   			
   			LogginModel logModel=new LogginModel();
   			logModel.messageId=messageId;
   			logModel.message=message;
   			logModel.logicalResourceId=logicalResourceId;
   			logModel.approximateReceiveCount=approximateReceiveCount;
   			logModel.approximateFirstReceiveTimestamp=approximateFirstReceiveTimestamp;
   			logModel.sentTimestamp=sentTimestamp;
   			logModel.receiptHandle=receiptHandle;
   			logModel.senderId=senderId;
   			logModel.contentType=contentType;
   			logModel.lookupDestination=lookupDestination;
   			try {
				logginService.addLoggin(logModel);
				String logText=messageId+"\n"+message+"\n"+logicalResourceId+
						"\n"+approximateReceiveCount+"\n"+approximateFirstReceiveTimestamp
						+"\n"+sentTimestamp+"\n"+receiptHandle
						+"\n"+senderId+"\n"+contentType
						+"\n"+lookupDestination;
				logginService.addLogginString(logText);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        System.out.println("Recieved File Message is>>>>>>>> "+file);
        ObjectMapper mapper = new ObjectMapper();
        String key="";
		try {
			 key = mapper.readValue(file, String.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        byte[] data = s3bucketService.getFile(key);
        ByteArrayResource resource = new ByteArrayResource(data);
        try {
			logginService.getLogginList();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + key + "\"")
                .body(resource);
    }
    @CrossOrigin
    @GetMapping(path = "/test")
    public ResponseEntity<String> test() throws IOException {
	    return ResponseEntity
	            .ok("Client app is working");
	}
}
